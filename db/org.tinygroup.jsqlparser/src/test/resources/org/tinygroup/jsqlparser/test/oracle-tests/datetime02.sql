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

select
	dbin.db_name,
	dbin.instance_name,
	dbin.version,
	case when s1.startup_time = s2.startup_time             then 0 else 1 end as bounce,
        cast(s1.end_interval_time as date) as begin_time,
	cast(s2.end_interval_time as date) as end_time,
	round((cast( (case when s2.end_interval_time > s1.end_interval_time then s2.end_interval_time else s1.end_interval_time end) as date)
		- cast(s1.end_interval_time as date)) * 86400) as int_secs,
	case when (s1.status <> 0 or s2.status <> 0) then 1 else 0 end as err_detect,
        round( greatest( (extract(day from s2.flush_elapsed) * 86400)
	       + (extract(hour from s2.flush_elapsed) * 3600)
	       + (extract(minute from s2.flush_elapsed) * 60)
	       + extract(second from s2.flush_elapsed),
	       (extract(day from s1.flush_elapsed) * 86400)
	       + (extract(hour from s1.flush_elapsed) * 3600)
	       + (extract(minute from s1.flush_elapsed) * 60)
	       + extract(second from s1.flush_elapsed),0 )
	) as max_flush_secs
from wrm$_snapshot s1 ,  wrm$_database_instance dbin ,  wrm$_snapshot s2
where s1.dbid = :dbid
and s2.dbid = :dbid
and s1.instance_number = :inst_num
and s2.instance_number = :inst_num
and s1.snap_id  = :bid
and s2.snap_id = :eid
and dbin.dbid = s1.dbid
and dbin.instance_number = s1.instance_number
and dbin.startup_time = s1.startup_time

