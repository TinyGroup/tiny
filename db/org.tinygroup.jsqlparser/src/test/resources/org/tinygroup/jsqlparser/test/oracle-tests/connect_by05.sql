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

with liste as (
  select substr(:liste, instr(','||:liste||',', ',', 1, rn),
  instr(','||:liste||',', ',', 1, rn+1) -
  instr(','||:liste||',', ',', 1, rn)-1) valeur
from (
  select rownum rn from dual
  connect by level<=length(:liste) - length(replace(:liste,',',''))+1))
select trim(valeur)
from liste
