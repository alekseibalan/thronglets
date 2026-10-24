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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * https://github.com/Unity-Technologies/UnityDataTools/blob/main/Documentation/command-serialized-file.md
 */
public class UnitySerializedFile {

  static class Constant {
    static final Set<Integer> PERSISTENT_TYPE_ID_SET = Set.of(
        1, // GameObject
        4, // Transform
        20, // Camera
        21, // Material
        23, // MeshRenderer
        28, // Texture2D
        33, // MeshFilter
        43, // Mesh
        48, // Shader
        49, // TextAsset
        54, // Rigidbody
        64, // MeshCollider
        65, // BoxCollider
        74, // AnimationClip
        81, // AudioListener
        82, // AudioSource
        83, // AudioClip
        84, // RenderTexture
        91, // AnimatorController
        95, // Animator
        96, // TrailRenderer
        111, // Animation
        114, // MonoBehaviour
        120, // LineRenderer
        128, // Font
        134, // PhysicMaterial
        135, // SphereCollider
        136, // CapsuleCollider
        150, // PreloadData
        164, // AudioReverbFilter
        165, // AudioHighPassFilter
        166, // AudioChorusFilter
        168, // AudioEchoFilter
        169, // AudioLowPassFilter
        170, // AudioDistortionFilter
        198, // ParticleSystem
        199, // ParticleSystemRenderer
        212, // SpriteRenderer
        213, // Sprite
        221, // AnimatorOverrideController
        222, // CanvasRenderer
        223, // Canvas
        224, // RectTransform
        225, // CanvasGroup
        241, // AudioMixerController
        243, // AudioMixerGroupController
        245, // AudioMixerSnapshotController
        320, // PlayableDirector
        328, // VideoPlayer
        329, // VideoClip
        331, // SpriteMask
        687078895 // SpriteAtlas
    );
  }

  List<Integer> typeList = new ArrayList<>();
  public List<UnityFile> fileList = new ArrayList<>();

  public UnitySerializedFile(byte[] bytes) {
    ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    // SerializedFile header (format, endianness)
    boolean endianness = false;
    long data01 = buffer.getLong();
    assert data01 == 0;
    int version = buffer.getInt();
    assert version == 22;
    int data02 = buffer.getInt();
    assert data02 == 0;
    long metadataSize = buffer.getLong();
    long fileSize = buffer.getLong();
    assert fileSize == bytes.length;
    long dataOffset = buffer.getLong();
    long data03 = buffer.getLong();
    assert data03 == 0;
    long metadataEnd = buffer.position() + metadataSize;
    assert dataOffset == (metadataEnd + 0x0F & 0xFFFFFFF0);
    buffer.order(endianness ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
    // SerializedFile metadata (enableTypeTree, typeTreeCount, serializedReferenceTypeTreeCount, sharedSubtreeCount)
    // (typeTrees, serializedReferenceTypeTrees, scriptTypes, sharedSubtrees)
    String unityVersion = getString(buffer);
    assert "2022.3.67f2".equals(unityVersion);
    int targetPlatform = buffer.getInt();
    assert targetPlatform == 13; // Android
    int data04 = buffer.get();
    assert data04 == 0;

    int typeCount = buffer.getInt();
    assert typeCount > 0 && typeCount < 352;
    for (int i = 0; i < typeCount; i++) {
      int persistentTypeID = buffer.getInt();
      assert Constant.PERSISTENT_TYPE_ID_SET.contains(persistentTypeID);
      typeList.add(persistentTypeID);
      int data11 = buffer.get();
      assert data11 == 0;
      int scriptTypeIndex = buffer.getShort();
      assert scriptTypeIndex == -1 || scriptTypeIndex >= 0 && scriptTypeIndex < typeCount;
      UUID data12 = getUUID(buffer);
      if (scriptTypeIndex >= 0) {
        UUID data13 = getUUID(buffer);
      }
    }

    int objectCount = buffer.getInt();
    assert objectCount > 0 && objectCount < 23551;
    while (buffer.position() % 4 != 0) {
      byte data21 = buffer.get();
      assert data21 == 0;
    }
    boolean fileSizeMatched = false;
    long expectedOffset = dataOffset;
    for (int i = 0; i < objectCount; i++) {
      long objectId = buffer.getLong();
      assert objectId == i + 1;
      long offset = buffer.getLong();
      assert offset >= 0;
      offset += dataOffset;
      assert offset == expectedOffset;
      int size = buffer.getInt();
      assert size > 0 && size < 8400000;
      long end = offset + size;
      assert end <= fileSize;
      fileSizeMatched |= end == fileSize;
      expectedOffset = end + 7 & ~7;
      int typeIndex = buffer.getInt();
      assert typeIndex >= 0 && typeIndex < typeCount;
      fileList.add(new UnityFile(typeList.get(typeIndex),
          Arrays.copyOfRange(bytes, (int) offset, (int) end),
          String.format("id: %d, offset: 0x%X, end: 0x%X", i, offset, end)));
    }
    assert fileSizeMatched;

    int count3 = buffer.getInt();
    assert count3 >= 0 && count3 < 301;
    for (int i = 0; i < count3; i++) {
      int data31 = buffer.getInt();
      assert data31 == 1 || data31 == 2;
      long data32 = buffer.getLong();
      assert data32 > 0 && data32 < 13313;
    }

    int count4 = buffer.getInt();
    for (int i = 0; i < count4; i++) {
      byte data41 = buffer.get();
      assert data41 == 0;
      long data42 = buffer.getLong();
      assert data42 == 0;
      int data43 = buffer.getInt();
      assert data43 >= 0 && data43 < 16;
      long data44 = buffer.getLong();
      assert data44 == 0;
      String data45 = getString(buffer);
      assert !data45.isEmpty();
    }

    int count5 = buffer.getInt();
    assert count5 >= 0;
    for (int i = 0; i < count5; i++) {
      int data51 = buffer.getInt();
      assert data51 == -1;
      int data52 = buffer.get();
      assert data52 == 0;
      long data53 = buffer.getLong();
      assert data53 == i;
      long data54 = buffer.getLong();
      assert data54 == 0;
      long data55 = buffer.getLong();
      assert data55 == 0;
      long data56 = buffer.getLong();
      assert data56 == 0;
      short data57 = buffer.getShort();
      assert data57 == 0;
    }

    byte data05 = buffer.get();
    assert data05 == 0;
    assert metadataEnd == buffer.position();
  }

  static String getString(ByteBuffer buffer) {
    int length = 0;
    while (buffer.get() != 0) length++;
    byte[] bytes = new byte[length];
    buffer.position(buffer.position() - length - 1);
    buffer.get(bytes);
    assert buffer.get() == 0;
    return new String(bytes);
  }

  static UUID getUUID(ByteBuffer buffer) {
    byte[] bytes = new byte[0x10];
    buffer.get(bytes); // keep buffer order
    ByteBuffer u = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
    return new UUID(u.getLong(), u.getLong());
  }

}
