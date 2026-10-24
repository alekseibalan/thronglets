/*
 * Copyright (C) 2026 Aleksei Balan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ab.unity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Set;

public class UnityFile {
  static final Set<Integer> NAME_PREFIX_TYPE = Set.of(28, 213);
  public String name;
  public int type;
  public byte[] bytes;
  public String debug;

  public UnityFile(int type, byte[] bytes, String debug) {
    this.type = type;
    this.bytes = bytes;
    this.debug = debug;
    this.name = "";
    if (NAME_PREFIX_TYPE.contains(type)) namePrefix();
  }

  private void namePrefix() {
    int end = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt() + 4;
    name = new String(Arrays.copyOfRange(bytes, 4, end));
    end = end + 3 & ~3;
    bytes = Arrays.copyOfRange(bytes, end, bytes.length - end);
  }
}
