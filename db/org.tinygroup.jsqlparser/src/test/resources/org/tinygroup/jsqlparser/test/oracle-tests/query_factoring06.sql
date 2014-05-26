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

with 
   dept_costs as (
      select department_name, sum(salary) dept_total
         from employees e, departments d
         where e.department_id = d.department_id
      group by department_name),
   avg_cost as (
      select sum(dept_total)/count(*) avg
      from dept_costs)
select * from dept_costs
   where dept_total >
      (select avvg from avg_cost)
      order by department_name


