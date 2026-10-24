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

package ab.playth;

import ab.unity.UnityFile;
import ab.unity.UnitySerializedFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) throws IOException {
    boolean assertsEnabled = false;
    assert assertsEnabled = true;
    if (!assertsEnabled) throw new AssertionError("java -ea");
    for (UnityFile file : new UnitySerializedFile(Files.readAllBytes(Paths.get("sharedassets2.assets"))).fileList) {
      if (!file.name.contains("Bored")) continue;
      System.out.println(file.debug + ", name: " + file.name);
    }
    for (Path path : Files.list(Paths.get(".")).collect(Collectors.toList()))
      if (path.toString().endsWith(".assets")) new UnitySerializedFile(Files.readAllBytes(path));
  }
}
