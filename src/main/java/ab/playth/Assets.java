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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class Assets {
  BufferedImage sprite(String name) {
    try {
      BufferedImage sprites = ImageIO.read(Files.newInputStream(Paths.get("Sprites-CAPTCHA.png")));
      // CaptchaSquare_Throng_Bored "m_Height": 53.923878, "m_Width": 45.847755, "m_X": 744.0761, "m_Y": 128
      // CaptchaSquare_Throng_SadA "m_Height": 53.923878, "m_Width": 45.847755, "m_X": 694.0761, "m_Y": 661
      UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
      int b = (int) (uuid.getLeastSignificantBits() & 1);
      return b > 0 ? subimage(sprites, 744, 128, 46, 54) : subimage(sprites, 694, 661, 46, 54);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  BufferedImage subimage(BufferedImage image, int x, int y, int w, int h) {
    return image.getSubimage(x, image.getHeight() - y - h, w, h);
  }
}
