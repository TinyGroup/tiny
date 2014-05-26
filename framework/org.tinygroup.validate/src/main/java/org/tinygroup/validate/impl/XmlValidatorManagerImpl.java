/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.validate.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.validate.Validator;
import org.tinygroup.validate.XmlValidatorManager;
import org.tinygroup.validate.config.BasicValidator;
import org.tinygroup.validate.config.ObjectValidator;
import org.tinygroup.validate.config.ObjectValidators;
import org.tinygroup.validate.config.Property;
import org.tinygroup.validate.config.PropertyValidatorConfig;
import org.tinygroup.validate.config.ValidatorConfig;

/**
 * xml校验管理器
 *
 * @author renhui
 */
public class XmlValidatorManagerImpl extends AbstractValidatorManger
        implements XmlValidatorManager {

    private static final String BEAN_INSTANCE_NOT_EXIST = "bean_instance_not_exist";

    List<ObjectValidator> objectValidatorConfigs = new ArrayList<ObjectValidator>();

    public void addObjectValidatorConfigs(ObjectValidators validatorConfigs) {
        for (ObjectValidator objectValidatorConfig : validatorConfigs.getValidatorConfigList()) {
            String beanName = objectValidatorConfig.getObjectBeanName();
            Object object = SpringUtil.getBean(beanName);
            if (object == null) {
                throw new TinySysRuntimeException(BEAN_INSTANCE_NOT_EXIST, beanName);
            }
            Class clazz = object.getClass();
            FieldValidatorMap fieldValidatorMap = getFieldValidatorMap(clazz);
            if (fieldValidatorMap == null) {
                fieldValidatorMap = new FieldValidatorMap();
            }
            for (PropertyValidatorConfig propertyValidatorConfig : objectValidatorConfig.getValidatorConfigList()) {
                try {
                    Field field = clazz.getDeclaredField(propertyValidatorConfig.getPropertyName());
					String wrapperKey = getWrapperKey(clazz, field);
                    FieldWapper fieldWapper = fieldWrapperMap.get(wrapperKey);
                    if (fieldWapper == null) {
                        fieldWapper = new FieldWapper(field, propertyValidatorConfig.getPropertyName(), propertyValidatorConfig.getPropertyTitle());
                        fieldWrapperMap.put(wrapperKey, fieldWapper);
                    }
                    if (propertyValidatorConfig.getValidatorConfigList().size() == 0) {
                        // 20130605 如果是该配置项没有ValidatorConfig
                        // 则为其生成一个空的validator添加
                        // 主要是为了添加一个filedWapper去fieldValidatorMap中占位
                        // 用于对象嵌套时，被嵌套对象的验证占位
                        fieldValidatorMap.addValidator(fieldWapper, "", null);
                        continue;
                    }
                    for (ValidatorConfig validatorConfig : propertyValidatorConfig.getValidatorConfigList()) {
                        fieldValidatorMap.addValidator(fieldWapper, validatorConfig.getScene(), getValidator(validatorConfig));
                    }

                } catch (Exception e) {
                    logger.errorMessage(e.getMessage(), e);
                    throw new RuntimeException(e);
                }

            }
            putClassFieldValidators(clazz, fieldValidatorMap);
        }

        for (BasicValidator basicValidator : validatorConfigs.getBasicConfigList()) {
            String name = basicValidator.getName();
            for (ValidatorConfig config : basicValidator.getValidatorConfigList()) {
                try {
                    Validator validator = getValidator(config);
                    putBasicValidators(name, validator);
                } catch (Exception e) {
                    logger.errorMessage("创建Validator时出错 name:{0},bean:{1},class:{2}", e, name, config.getValidatorBeanName(), config.getValidatorClassName());
                    throw new RuntimeException(e);
                }
            }
        }
    }
    

    public void removeObjectValidatorConfigs(ObjectValidators validatorConfigs) {
    	 for (ObjectValidator objectValidatorConfig : validatorConfigs.getValidatorConfigList()) {
    		 String beanName = objectValidatorConfig.getObjectBeanName();
             Object object = SpringUtil.getBean(beanName);
             if (object == null) {
                 throw new TinySysRuntimeException(BEAN_INSTANCE_NOT_EXIST, beanName);
             }
             Class clazz = object.getClass();
             removeFieldValidatorMap(clazz);
    	 }
    	 for (BasicValidator basicValidator : validatorConfigs.getBasicConfigList()) {
             String name = basicValidator.getName();
             for (ValidatorConfig config : basicValidator.getValidatorConfigList()) {
                 try {
                     Validator validator = getValidator(config);
                     removeBasicValidators(name, validator);
                 } catch (Exception e) {
                     logger.errorMessage("创建Validator时出错 name:{0},bean:{1},class:{2}", e, name, config.getValidatorBeanName(), config.getValidatorClassName());
                     throw new RuntimeException(e);
                 }
             }
         }
	}



	/**
     * 根据beanname和classname获取校验器,classname优先级高
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Validator getValidator(ValidatorConfig validatorConfig)
            throws IllegalAccessException, InvocationTargetException {
        String validatorBeanName = validatorConfig.getValidatorBeanName();
        String validatorClassName = validatorConfig.getValidatorClassName();
        Validator validator = getValidator(validatorClassName);
        if (validator == null) {
            validator = SpringUtil.getBean(validatorBeanName);
        }
        for (Property property : validatorConfig.getProperties()) {
            BeanUtils.setProperty(validator, property.getName(), property.getValue());
        }

        return validator;
    }

}
