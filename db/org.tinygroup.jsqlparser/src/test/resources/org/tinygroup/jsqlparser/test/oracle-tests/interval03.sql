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
 interval '4 5:12:10.222' day to second(3)
,interval '4 5:12' day to minute
,interval '400 5' day(3) to hour
,interval '400' day(3)
,interval '11:12:10.2222222' hour to second(7)
,interval '11:20' hour to minute
,interval '10' hour
,interval '10:22' minute to second
,interval '10' minute
,interval '4' day
,interval '25' hour
,interval '40' minute
,interval '120' hour(3)
,interval '30.12345' second(2,4)
,interval :a day
from dual
