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
package org.tinygroup.service.impl;

import org.tinygroup.context.Context;
import org.tinygroup.event.Parameter;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.i18n.I18nMessages;
import org.tinygroup.service.Service;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.exception.ParameterCheckFailException;
import org.tinygroup.service.exception.ServiceNotExistException;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;

import java.util.List;

public class ServiceProviderImpl implements ServiceProviderInterface {

    private static final String OUTPUT = "输出";
    private static final String INPUT = "输入";
    private ServiceRegistry serviceRegistory = null;
    private I18nMessages i18nMessages = I18nMessageFactory.getI18nMessages();

    public void setServiceRegistory(ServiceRegistry serviceRegistory) {
        this.serviceRegistory = serviceRegistory;
    }

    public ServiceRegistry getServiceRegistory() {
        return serviceRegistory;
    }

    public void validateInputParameter(Service service, Context Context) {
        ServiceRegistryItem serviceRegistryItem = serviceRegistory.getServiceRegistryItem(service);
        validateParameter(serviceRegistryItem.getParameters(), Context, INPUT);
    }

    /**
     * 检查参数 TODO 没有处理方向
     *
     * @param parameterDescriptors
     * @param context
     * @param context
     * @throws ParameterCheckFailException
     */
    private void validateParameter(List<Parameter> parameterDescriptors, Context context, String direct) {

        if (parameterDescriptors != null) {
            for (Parameter parameterDescriptor : parameterDescriptors) {
                Object object = context.get(parameterDescriptor.getName());
                if (parameterDescriptor.isRequired() && object == null) {
                    throw new ParameterCheckFailException(i18nMessages.getMessage("service.paramIsNotExist", parameterDescriptor.getName()));
                }

                if (object.getClass().isArray() != parameterDescriptor.isArray()) {
                    throw new ParameterCheckFailException(i18nMessages.getMessage("service.paramArrayNotSuit", parameterDescriptor.getName(), parameterDescriptor.isArray(), object.getClass().isArray()));
                }
                if (!object.getClass().getName().equals(parameterDescriptor.getType())) {
                    throw new ParameterCheckFailException(i18nMessages.getMessage("service.paramNotSuit", parameterDescriptor.getName(), object.getClass().getClass().getName(), parameterDescriptor.getType()));
                }
            }
        }
    }

    public void validateOutputParameter(Service service, Context context) {
        ServiceRegistryItem serviceRegistryItem = serviceRegistory.getServiceRegistryItem(service);
        validateParameter(serviceRegistryItem.getResults(), context, OUTPUT);

    }

    public void execute(Service service, Context context) {
        service.execute(context);

    }

    public void execute(String serviceId, Context context) {
        ServiceRegistryItem serviceRegistryItem = serviceRegistory.getServiceRegistryItem(serviceId);
        if (serviceRegistryItem != null) {
            serviceRegistryItem.getService().execute(context);
        } else {
            throw new ServiceNotExistException();
        }
    }


    public <T> void setConfig(T config) {

    }

    public Service getService(String serviceId) {
        ServiceRegistryItem item = serviceRegistory.getServiceRegistryItem(serviceId);
        if (item == null)
            return null;
        return item.getService();
    }

    public ServiceRegistryItem getServiceRegistryItem(Service service) {

        return serviceRegistory.getServiceRegistryItem(service);
    }

}
