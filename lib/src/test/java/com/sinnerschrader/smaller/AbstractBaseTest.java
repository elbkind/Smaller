package com.sinnerschrader.smaller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.sinnerschrader.smaller.common.Manifest;
import com.sinnerschrader.smaller.common.SmallerException;
import com.sinnerschrader.smaller.lib.Result;

/**
 * @author marwol
 */
public abstract class AbstractBaseTest {

  protected abstract void runToolChain(final String file,
      final ToolChainCallback callback) throws Exception;

  protected Manifest getManifest(final File sourceDir) throws IOException {
    return new ObjectMapper().readValue(getMainFile(sourceDir), Manifest.class);
  }

  private File getMainFile(final File input) {
    File main = new File(input, "META-INF/MAIN.json");
    if (!main.exists()) {
      // Old behaviour: Search directly in root of zip
      main = new File(input, "MAIN.json");
      if (!main.exists()) {
        throw new SmallerException(
            "Missing instructions file 'META-INF/MAIN.json'");
      }
    }
    return main;
  }

  protected static void assertOutput(final String result, final String expected) {
    System.out.println("Expected: " + expected.replace("\n", "\\n"));
    System.out.println("Result:   " + result.replace("\n", "\\n"));
    assertThat(result, is(expected));
  }

  protected interface ToolChainCallback {

    void test(Result result) throws Exception;

  }

}
