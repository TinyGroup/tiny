--
--  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
--
--  Licensed under the GPL, Version 3.0 (the "License");
--  you may not use this file except in compliance with the License.
--  You may obtain a copy of the License at
--
--       http://www.gnu.org/licenses/gpl.html
--
--  Unless required by applicable law or agreed to in writing, software
--  distributed under the License is distributed on an "AS IS" BASIS,
--  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--  See the License for the specific language governing permissions and
--  limitations under the License.
--

-- http://www.adp-gmbh.ch/ora/sql/model_clause/ex_generate_dates.html
select dt from (select trunc(sysdate) dt from dual)
model 
  dimension by (0 d)
  measures     (dt)
  rules iterate(9) (
    dt[ iteration_number+1 ] = dt[ iteration_number ]+1
  )
