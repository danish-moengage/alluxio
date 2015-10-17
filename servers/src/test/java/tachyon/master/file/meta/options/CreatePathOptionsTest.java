/*
 * Licensed to the University of California, Berkeley under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package tachyon.master.file.meta.options;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import tachyon.Constants;
import tachyon.client.ClientContext;
import tachyon.client.UnderStorageType;
import tachyon.conf.TachyonConf;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ClientContext.class)
public class CreatePathOptionsTest {
  @Test
  public void builderTest() {
    Random random = new Random();
    long blockSize = random.nextLong();
    boolean directory = random.nextBoolean();
    long operationTimeMs = random.nextLong();
    boolean persisted = random.nextBoolean();
    boolean recursive = random.nextBoolean();
    long ttl = random.nextLong();

    CreatePathOptions options =
        new CreatePathOptions.Builder(new TachyonConf())
            .setBlockSizeBytes(blockSize)
            .setDirectory(directory)
            .setOperationTimeMs(operationTimeMs)
            .setPersisted(persisted)
            .setRecursive(recursive)
            .setTTL(ttl)
            .build();

    Assert.assertEquals(blockSize, options.getBlockSizeBytes());
    Assert.assertEquals(directory, options.isDirectory());
    Assert.assertEquals(operationTimeMs, options.getOperationTimeMs());
    Assert.assertEquals(persisted, options.isPersisted());
    Assert.assertEquals(recursive, options.isRecursive());
    Assert.assertEquals(ttl, options.getTTL());
  }

  @Test
  public void defaultsTest() {
    TachyonConf conf = new TachyonConf();
    conf.set(Constants.USER_BLOCK_SIZE_BYTES_DEFAULT, "64MB");
    Whitebox.setInternalState(ClientContext.class, "sTachyonConf", conf);

    CreatePathOptions options = CreatePathOptions.defaults();

    Assert.assertEquals(64 * Constants.MB, options.getBlockSizeBytes());
    Assert.assertFalse(options.isDirectory());
    Assert.assertFalse(options.isPersisted());
    Assert.assertFalse(options.isRecursive());
    Assert.assertEquals(Constants.NO_TTL, options.getTTL());
  }
}
