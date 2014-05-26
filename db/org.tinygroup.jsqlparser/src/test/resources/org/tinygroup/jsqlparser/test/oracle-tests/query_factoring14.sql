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

with emp_count (eid, emp_last, mgr_id, mgrlevel, salary, cnt_employees) as (
  select employee_id, last_name, manager_id, 0 mgrlevel, salary, 0 cnt_employees
  from employees
  union all
  select e.employee_id, e.last_name, e.manager_id, r.mgrlevel+1 mgrlevel, e.salary, 1
  cnt_employees
  from emp_count r, employees e
  where e.employee_id = r.mgr_id)
search depth first by emp_last set order1
select emp_last, eid, mgr_id, salary, sum(cnt_employees), max(mgrlevel) mgrlevel
from emp_count
group by emp_last, eid, mgr_id, salary
having max(mgrlevel) > 0
order by mgr_id nulls first, emp_last
