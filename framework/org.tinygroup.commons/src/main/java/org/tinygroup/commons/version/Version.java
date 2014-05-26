/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.commons.version;

/**
 * 版本号规范
 * d.d.d.d.d.d-snapshot|其他字串
 * @author luoguo
 *
 */
class Version implements Comparable<Version> {
	private int[] versions;
	private boolean snapshot;
	private String extVersion;

	public Version(String version) {
		String string[] = version.split("-");
		if (string.length == 2) {
			if (string[1].equalsIgnoreCase("snapshot")) {
				snapshot = true;
			} else {
				extVersion = string[1];
			}
		}
		String[] dStrs = string[0].split("[.]");
		versions = new int[dStrs.length];
		for (int i = 0; i < dStrs.length; i++) {
			versions[i] = Integer.valueOf(dStrs[i]);
		}
	}

	public int[] getVersions() {
		return versions;
	}

	public void setVersions(final int[] versions) {
		this.versions = versions.clone();
	}

	public boolean isSnapshot() {
		return snapshot;
	}

	public void setSnapshot(boolean snapshot) {
		this.snapshot = snapshot;
	}

	public int compareTo(Version destVersion) {
		int len = versions.length > destVersion.versions.length ? destVersion.versions.length
				: versions.length;
		int ret = 0;
		for (int i = 0; i < len; i++) {
			if (versions[i] < destVersion.versions[i]) {
				ret = -1;
				break;
			} else if (versions[i] > destVersion.versions[i]) {
				ret = 1;
				break;
			}
		}
		if (ret == 0) {
			if (versions.length > destVersion.versions.length) {
				ret = 1;
			} else if (versions.length < destVersion.versions.length) {
				ret = -1;
			} else {
				if (!snapshot && destVersion.snapshot) {
					return 1;
				} else if (snapshot && !destVersion.snapshot) {
					return -1;
				} else if (extVersion != null || destVersion.extVersion != null) {
					throw new RuntimeException("不能确定的两个版本.");
				}
			}
		}
		return ret;
	}

}
