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
package org.tinygroup.jsqlparser.test.select;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserUtil;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;
import static org.tinygroup.jsqlparser.test.TestUtils.assertSqlCanBeParsedAndDeparsed;

/**
 * Tries to parse and deparse all statments in
 * org.tinygroup.jsqlparser.test.oracle-tests.
 * 
 * As a matter of fact there are a lot of files that can still not processed.
 * Here a step by step improvement is the way to go.
 * 
 * The test ensures, that the successfull parsed file count does not decrease.
 *
 * @author toben
 */
public class SpecialOracleTest {

    private static final File SQLS_DIR = new File("target/test-classes/org/tinygroup/jsqlparser/test/oracle-tests");
    private static final Logger LOG = Logger.getLogger(SpecialOracleTest.class.getName());

    @Test
    public void testAllSqlsParseDeparse() throws IOException {
        int count=0;
        int success=0;
        File[] sqlTestFiles = SQLS_DIR.listFiles();

        for (File file : sqlTestFiles) {
            if (file.isFile()) {
                count++;
                LOG.log(Level.INFO, "testing {0}", file.getName());
                String sql = FileUtils.readFileToString(file);
                try {
                    assertSqlCanBeParsedAndDeparsed(sql, true);
                    success++;
                    LOG.info("   -> SUCCESS");
                } catch (JSQLParserException ex) {
                    //LOG.log(Level.SEVERE, null, ex);
                    LOG.info("   -> PROBLEM");
                }
            }
        }
        
        LOG.log(Level.INFO, "tested {0} files. got {1} correct parse results", new Object[]{count, success});
        assertTrue(success>100);
    }

    @Test
    public void testAllSqlsOnlyParse()
            throws IOException {
        File[] sqlTestFiles = new File(SQLS_DIR, "only-parse-test").listFiles();

        for (File file : sqlTestFiles) {
            LOG.log(Level.INFO, "testing {0}", file.getName());
            String sql = FileUtils.readFileToString(file);
            try {
                CCJSqlParserUtil.parse(sql);

                LOG.info("   -> SUCCESS");
            } catch (JSQLParserException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}
