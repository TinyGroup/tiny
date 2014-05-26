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
package org.tinygroup.jsqlparser.expression.operators.relational;

public interface SupportsOldOracleJoinSyntax {

	int NO_ORACLE_JOIN = 0;
	int ORACLE_JOIN_RIGHT = 1;
	int ORACLE_JOIN_LEFT = 2;

	int getOldOracleJoinSyntax();

	void setOldOracleJoinSyntax(int oldOracleJoinSyntax);

	int NO_ORACLE_PRIOR = 0;
	int ORACLE_PRIOR_START = 1;
	int ORACLE_PRIOR_END = 2;

	int getOraclePriorPosition();

	void setOraclePriorPosition(int priorPosition);
}
