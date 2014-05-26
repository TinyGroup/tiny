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

select  count(*), max(scn)
from
(
	select sp.bo#, sp.pmoptype, sp.scn, sp.flags
	from sumpartlog$ sp, sumdep$ sd
	where sd.sumobj# = :1 and sd.p_obj# = sp.bo#
	group by sp.bo#, sp.pmoptype, sp.scn, sp.flags
	minus
	select sp.bo#, sp.pmoptype, sp.scn, sp.flags
	from sumpartlog$ sp
	where sp.bo# not in
	(
		select sk.detailobj# from sumkey$ sk where sk.sumobj# = :1 and sk.detailcolfunction in (2,3)
	)
	and bitand(sp.flags, 2) != 2 and sp.pmoptype in (2,3,5,7)
	group by sp.bo#, sp.pmoptype, sp.scn, sp.flags
)
where scn > : 2
