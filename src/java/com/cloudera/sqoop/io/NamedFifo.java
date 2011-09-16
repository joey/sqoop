/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudera.sqoop.io;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.util.Shell;

/**
 * A named FIFO channel.
 */
public class NamedFifo {

  private File fifoFile;

  /** Create a named FIFO object at the local fs path given by 'pathname'. */
  public NamedFifo(String pathname) {
    this.fifoFile = new File(pathname);
  }

  /** Create a named FIFO object at the local fs path given by the 'fifo' File
   * object. */
  public NamedFifo(File fifo) {
    this.fifoFile = fifo;
  }

  /**
   * Return the File object representing the FIFO.
   */
  public File getFile() {
    return this.fifoFile;
  }

  /**
   * Create a named FIFO object.
   * The pipe will be created with permissions 0600.
   * @throws IOException on failure.
   */
  public void create() throws IOException {
    create(0600);
  }

  /**
   * Create a named FIFO object with the specified fs permissions.
   * This depends on the 'mkfifo' system utility existing. (for example,
   * provided by Linux coreutils). This object will be deleted when
   * the process exits.
   * @throws IOException on failure.
   */
  public void create(int permissions) throws IOException {
    String filename = fifoFile.toString();

    // Format permissions as a mode string in base 8.
    String modeStr = Integer.toString(permissions, 8);

    // Create the FIFO itself.
    Shell.execCommand("mkfifo", "-m", "0" + modeStr, filename);

    // Schedule the FIFO to be cleaned up when we exit.
    this.fifoFile.deleteOnExit();
  }
}

