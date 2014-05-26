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

select   bio, rtrim (str_new, ';') new_str
  from   db_temp
model
   partition by (rownum rn)
dimension by (0 dim)
   measures (bio, bio || ';' str_new)
   rules
      iterate (1000) until (str_new[0] = previous (str_new[0]))
      (str_new [0] =
            regexp_replace (str_new[0], '(^|;)([^;]+;)(.*?;)?\2+', '\1\2\3'));
