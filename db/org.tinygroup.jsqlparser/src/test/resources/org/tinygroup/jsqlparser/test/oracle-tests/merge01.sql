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

merge into bonuses d 
   using (select employee_id.* from employees) s 
   on (employee_id = a) 
   when matched then update set d.bonus = bonus 
     delete where (salary > 8000)
   when not matched then insert (d.employee_id, d.bonus) 
     values (s.employee_id, s.salary)
     where (s.salary <= 8000)

