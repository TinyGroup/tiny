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

 select metric_id ,bsln_guid ,timegroup ,obs_value as obs_value 
 , cume_dist () over (partition by metric_id, bsln_guid, timegroup order by obs_value ) as cume_dist 
 , count(1) over (partition by metric_id, bsln_guid, timegroup ) as n 
 , row_number () over (partition by metric_id, bsln_guid, timegroup order by obs_value) as rrank 
 , percentile_disc(:b7 ) within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as mid_tail_value 
 , max(obs_value) over (partition by metric_id, bsln_guid, timegroup ) as max_val 
 , min(obs_value) over (partition by metric_id, bsln_guid, timegroup ) as min_val 
 , avg(obs_value) over (partition by metric_id, bsln_guid, timegroup ) as avg_val 
 , stddev(obs_value) over (partition by metric_id, bsln_guid, timegroup ) as sdev_val 
 , percentile_cont(0.25) within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as pctile_25 
 , percentile_cont(0.5)  within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as pctile_50 
 , percentile_cont(0.75) within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as pctile_75 
 , percentile_cont(0.90) within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as pctile_90 
 , percentile_cont(0.95) within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as pctile_95 
 , percentile_cont(0.99) within group (order by obs_value asc) over (partition by metric_id, bsln_guid, timegroup) as pctile_99
 from timegrouped_rawdata d

