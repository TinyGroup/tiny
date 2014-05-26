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

 select *
 from s join d using(c)
 pivot
 (
 max(c_c_p) as max_ccp
 , max(d_c_p) max_dcp
 , max(d_x_p) dxp
 , count(1) cnt
 for (i, p) in
 (
 (1,1) as one_one,
 (1,2) as one_two,
 (1,3) as one_three,
 (2,1) as two_one,
 (2,2) as two_two,
 (2,3) as two_three
 )
 )
 where d_t = 'p'
