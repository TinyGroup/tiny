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

select channels.channel_desc, calendar_month_desc, 
       countries.country_iso_code,
       to_char(sum(amount_sold), '9,999,999,999') sales$
from sales, customers, times, channels, countries
where sales.time_id=times.time_id 
  and sales.cust_id=customers.cust_id 
  and customers.country_id = countries.country_id
  and sales.channel_id = channels.channel_id 
  and channels.channel_desc in ('direct sales', 'internet') 
  and times.calendar_month_desc in ('2000-09', '2000-10') 
  and countries.country_iso_code in ('gb', 'us')
group by 
  rollup(channels.channel_desc, calendar_month_desc, countries.country_iso_code)