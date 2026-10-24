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

import ab.jnc3.Screen;

import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainSprites {

  public static void main(String[] args) {
    Assets assets = new Assets();
    BufferedImage[] b = new BufferedImage[12];
    for (int i = 0; i < b.length; i++) b[i] = assets.sprite("Char_ThrongA_Bored_KickRock_Spritesheet_" + i);
    Screen screen = new Screen();
    Queue<String> keyListener = new LinkedBlockingQueue<>();
    screen.keyListener = keyListener::add;
    boolean open = true;
    while (open) {
      while (!keyListener.isEmpty()) if ("Esc".equals(keyListener.remove())) open = false;
      for (int i = 0; i < b.length; i++) screen.image.getGraphics().drawImage(b[i], i % 4 * 80, i / 4  * 80, null);
      screen.update();
      try { Thread.sleep(100); } catch (InterruptedException e) {}
    }
    screen.close();
  }
}
