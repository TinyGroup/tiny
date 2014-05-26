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

with o(obj,link) as
(
select 'a', 'b' from dual union all
select 'a', 'c' from dual union all
select      'c', 'd' from dual union all
select           'd', 'c' from dual union all
select           'd', 'e' from dual union all
select                'e', 'e' from dual
),
t(root,lev,obj,link,path) as (
select obj,1,obj,link,cast(obj||'->'||link 
as varchar2(4000))
from o 
where obj='a'  -- start with
union all
select 
  t.root,t.lev+1,o.obj,o.link,
  t.path||', '||o.obj||
    '->'
    ||o.link
from t, o 
where t.link=o.obj
)
search depth first by obj set ord
cycle obj set cycle to 1 default 0
select root,lev,obj,link,path,cycle,
    case
    when (lev - lead(lev) over (order by ord)) < 0
    then 0
    else 1
    end is_leaf
 from t

