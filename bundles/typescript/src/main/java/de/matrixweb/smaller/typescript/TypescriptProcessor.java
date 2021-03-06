package de.matrixweb.smaller.typescript;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import de.matrixweb.nodejs.NodeJsExecutor;
import de.matrixweb.smaller.common.SmallerException;
import de.matrixweb.smaller.resource.Processor;
import de.matrixweb.smaller.resource.Resource;
import de.matrixweb.smaller.resource.Type;
import de.matrixweb.vfs.VFS;

/**
 * @author marwol
 */
public class TypescriptProcessor implements Processor {

  private NodeJsExecutor node;

  /**
   * @see de.matrixweb.smaller.resource.Processor#supportsType(de.matrixweb.smaller.resource.Type)
   */
  @Override
  public boolean supportsType(final Type type) {
    return type == Type.JS;
  }

  /**
   * @see de.matrixweb.smaller.resource.Processor#execute(de.matrixweb.vfs.VFS,
   *      de.matrixweb.smaller.resource.Resource, java.util.Map)
   */
  @Override
  public Resource execute(final VFS vfs, final Resource resource,
      final Map<String, Object> options) throws IOException {
    if (this.node == null) {
      try {
        this.node = new NodeJsExecutor();
        this.node.setModule(getClass(), "typescript-0.9.1");
      } catch (final IOException e) {
        this.node = null;
        throw new SmallerException("Failed to setup node for typescript", e);
      }
    }
    final String outfile = this.node.run(vfs,
        resource != null ? resource.getPath() : null, options);
    Resource result = resource;
    if (resource != null) {
      if (outfile != null) {
        result = resource.getResolver().resolve(outfile);
      } else {
        result = resource.getResolver().resolve(
            FilenameUtils.removeExtension(resource.getPath()) + ".js");
      }
    }
    return result;
  }

  /**
   * @see de.matrixweb.smaller.resource.Processor#dispose()
   */
  @Override
  public void dispose() {
    if (this.node != null) {
      this.node.dispose();
    }
  }

}
