/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.mahout.ga.watchmaker.cd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * 
 */
public class FileInfosDatasetTest extends TestCase {

  public void testRanges() throws IOException {
    FileSystem fs = FileSystem.get(new Configuration());
    Path inpath = new Path("build/test-classes/wdbc");
    
    DataSet dataset = FileInfoParser.parseFile(fs, inpath);
    DataSet.initialize(dataset);

    String filename = "build/test-classes/wdbc/wdbc.data";
    BufferedReader in = new BufferedReader(new FileReader(filename));

    String line;
    DataLine dl = new DataLine();
    while ((line = in.readLine()) != null) {
      dl.set(line);
      for (int index = 0; index < dataset.getNbAttributes(); index++) {
        if (dataset.isNumerical(index)) {
          assertInRange(dl.getAttribut(index), dataset.getMin(index), dataset
              .getMax(index));
        } else {
          assertInRange(dl.getAttribut(index), 0, dataset.getNbValues(index));
        }
      }
    }
  }

  private void assertInRange(double value, double min, double max) {
    TestCase.assertTrue("value"+value+") < min", value >= min);
    TestCase.assertTrue("value("+value+") > max", value <= max);
  }

}
