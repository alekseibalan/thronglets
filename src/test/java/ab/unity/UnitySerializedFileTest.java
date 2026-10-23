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

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UnitySerializedFileTest {

  @Test
  void getUUID() {
    byte[] bytes = new byte[]{
        'U', 'n', 'i', 't', 'y', 0,
        (byte) 0xd4, (byte) 0x1d, (byte) 0x8c, (byte) 0xd9, (byte) 0x8f, (byte) 0x00, (byte) 0xb2, (byte) 0x04,
        (byte) 0xe9, (byte) 0x80, (byte) 0x09, (byte) 0x98, (byte) 0xec, (byte) 0xf8, (byte) 0x42, (byte) 0x7e,
    };
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    assertEquals("Unity", UnitySerializedFile.getString(buffer));
    assertEquals(UUID.fromString("d41d8cd9-8f00-b204-e980-0998ecf8427e"), UnitySerializedFile.getUUID(buffer));
    assertFalse(buffer.hasRemaining());
  }
}
