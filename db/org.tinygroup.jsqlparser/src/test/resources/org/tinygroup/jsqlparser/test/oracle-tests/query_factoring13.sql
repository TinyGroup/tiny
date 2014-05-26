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

with dup_hiredate (eid, emp_last, mgr_id, reportlevel, hire_date, job_id) as (
  select employee_id, last_name, manager_id, 0 reportlevel, hire_date, job_id
  from employees
  where manager_id is null
  union all
  select e.employee_id, e.last_name, e.manager_id,
  r.reportlevel+1 reportlevel, e.hire_date, e.job_id
  from dup_hiredate r, employees e
  where r.eid = e.manager_id)
search depth first by hire_date set order1
cycle hire_date set is_cycle to 'y' default 'n'
select lpad(' ',2*reportlevel)||emp_last emp_name, eid, mgr_id, hire_date, job_id, is_cycle
from dup_hiredate
order by order1
