package org.sugarj.stdlib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParseException;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.BadTokenException;
import org.spoofax.jsglr.shared.SGLRException;
import org.spoofax.jsglr.shared.TokenExpectedException;
import org.sugarj.common.ATermCommands;
import org.sugarj.common.FileCommands;
import org.sugarj.common.path.AbsolutePath;
import org.sugarj.common.path.Path;
import org.sugarj.common.path.RelativePath;

/**
 * @author Sebastian Erdweg <seba at informatik uni-marburg de>
 */
public class StdLib {

  public static final String VERSION = "stdlib-0.3";
  
  public static Path stdLibDir;
  private static Path stdLibTmpDir;
  static {

    String thisClassPath = "org/sugarj/stdlib/StdLib.class";
    URL thisClassURL = StdLib.class.getClassLoader().getResource(thisClassPath);
    
    if (thisClassURL.getProtocol().equals("bundleresource"))
      try {
        thisClassURL = FileLocator.resolve(thisClassURL);
      } catch (IOException e) {
        e.printStackTrace();
      }
    
    String classPath;
    try {
      classPath = new File(thisClassURL.toURI()).getAbsolutePath();
    } catch (URISyntaxException e) {
      classPath = new File(thisClassURL.getPath()).getAbsolutePath();
    }
    String binPath = classPath.substring(0, classPath.length() - thisClassPath.length());
    
    try {
      stdLibDir = new AbsolutePath(binPath);
      stdLibTmpDir = FileCommands.newTempDir();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Path ensureFile(String resource) {
    Path f = new RelativePath(stdLibDir, resource);
    if (FileCommands.exists(f))
      return f;
    
    f = new RelativePath(stdLibTmpDir, resource);
    f.getFile().getParentFile().mkdirs();
    
    try {
      InputStream in = StdLib.class.getClassLoader().getResourceAsStream(resource);
      if (in == null)
        return new RelativePath(stdLibDir, resource);
      
      FileOutputStream fos = new FileOutputStream(f.getFile());
      int len = -1;
      byte[] bs = new byte[256];
      while ((len = in.read(bs)) >= 0)
        fos.write(bs, 0, len);
      fos.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return f;
  }
  
  public final static Path sdfDef = ensureFile("org/sugarj/languages/Sdf2.def");
  public final static Path sdfTbl = ensureFile("org/sugarj/languages/Sdf2.tbl");
  public static SGLR sdfParser;
  static {
    try {
      sdfParser = new SGLR(new TreeBuilder(), ATermCommands.parseTableManager.loadFromFile(StdLib.sdfTbl.getAbsolutePath()));
    } catch (IOException | InvalidParseTableException e) {
      e.printStackTrace();
    }
  }
  
  public final static Path strategoDef = ensureFile("org/sugarj/languages/Stratego.def");
  public final static Path strategoTbl = ensureFile("org/sugarj/languages/Stratego.tbl");
  public static SGLR strategoParser;
  static {
    try {
      strategoParser = new SGLR(new TreeBuilder(), ATermCommands.parseTableManager.loadFromFile(StdLib.strategoTbl.getAbsolutePath()));
    } catch (IOException | InvalidParseTableException e) {
      e.printStackTrace();
    }
  }
  
  public final static Path editorServicesDef = ensureFile("org/sugarj/languages/EditorServices.def");
  public final static Path editorServicesTbl = ensureFile("org/sugarj/languages/EditorServices.tbl");
  public final static Path stdEditor = ensureFile("org/sugarj/stdlib/StdEditor.serv");
  public static SGLR editorServicesParser;
  public static List<IStrategoTerm> stdEditirServices;
  static {
    try {
      editorServicesParser = new SGLR(new TreeBuilder(), ATermCommands.parseTableManager.loadFromFile(StdLib.editorServicesTbl.getAbsolutePath()));
      stdEditirServices = ATermCommands.parseEditorServiceFile(editorServicesParser, StdLib.stdEditor);
    } catch (IOException | InvalidParseTableException | SGLRException | InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public static Path plainDef = ensureFile("org/sugarj/languages/Plain.def");
  public static Path commonDef = ensureFile("org/sugarj/stdlib/Common.def");
  public static Path sugarDef = ensureFile("org/sugarj/languages/Sugar.def");
  public static Path modelDef = ensureFile("org/sugarj/languages/Model.def");
  
  public static Path failureTrans = ensureFile("failure-trans.jar");

  public static List<Path> stdGrammars() {
    return Arrays.asList(new Path[]{sdfDef, strategoDef, editorServicesDef, plainDef, commonDef, sugarDef, modelDef});
  }

  public static void main(String args[]) {
    exists(stdLibDir);
    exists(sdfDef);
    exists(sdfTbl);
    exists(strategoDef);
    exists(strategoTbl);
    exists(editorServicesDef);
    exists(editorServicesTbl);
    exists(plainDef);
    exists(commonDef);
    exists(sugarDef);
    exists(stdEditor);
    exists(failureTrans);
  }
  
  private static void exists(Path file) {
    if (FileCommands.exists(file))
      System.out.println(file + " exists.");
    else
      System.err.println(file + " does not exist.");
  }
}
