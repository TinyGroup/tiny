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

-- insert when
insert 
when (deptno=10) then
  into emp_10 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
when (deptno=20) then
  into emp_20 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
when (deptno=30) then
  into emp_30 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
else
  into leftover (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
select * from emp
