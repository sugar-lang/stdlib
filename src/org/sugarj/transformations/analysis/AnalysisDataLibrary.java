package org.sugarj.transformations.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.spoofax.interpreter.core.IContext;
import org.spoofax.interpreter.core.InterpreterException;
import org.spoofax.interpreter.library.AbstractPrimitive;
import org.spoofax.interpreter.library.AbstractStrategoOperatorRegistry;
import org.spoofax.interpreter.library.ssl.StrategoHashMap;
import org.spoofax.interpreter.stratego.Strategy;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.StrategoString;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;

public class AnalysisDataLibrary extends AbstractStrategoOperatorRegistry {

  public static final String REGISTRY_NAME = "SUGARJ_ANALYSIS";

  public static AnalysisDataLibrary instance = new AnalysisDataLibrary();
  
  private AnalysisDataLibrary() { 
    add(new Get());
    add(new Put());
    add(new Remove());
    add(new GetAll());
    add(new PutAll());
  }

  @Override
  public String getOperatorRegistryName() {
    return REGISTRY_NAME;
  }

  private final static WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData = new WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>>();

  public static class Get extends AbstractPrimitive {
    public Get() {
      super(REGISTRY_NAME + "_get_data", 0, 1);
    }

    @Override
    public boolean call(IContext context, Strategy[] ss, IStrategoTerm[] ts) throws InterpreterException {
      IStrategoTerm term = context.current();
      IStrategoTerm key = ts[0];

      if (key.getTermType() != IStrategoTerm.STRING)
        throw new InterpreterException(this.getName() + " requires a string as term argument");

      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return false;

      IStrategoTerm val = map.get(Term.asJavaString(key));
      if (val == null)
        return false;
    
      context.setCurrent(val);
      return true;
    }
  }
  
  public static class Put extends AbstractPrimitive {
    public Put() {
      super(REGISTRY_NAME + "_put_data", 0, 2);
    }

    @Override
    public boolean call(IContext context, Strategy[] ss, IStrategoTerm[] ts) throws InterpreterException {
      IStrategoTerm term = context.current();
      IStrategoTerm key = ts[0];
      IStrategoTerm val = ts[1];

      if (key.getTermType() != IStrategoTerm.STRING)
        throw new InterpreterException(this.getName() + " requires a string as first term argument");

      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null) {
        map = new HashMap<String, IStrategoTerm>();
        analysisData.put(term, map);
      }
      
      map.put(Term.asJavaString(key), val);

      return true;
    }
  }
  
  public static class Remove extends AbstractPrimitive {
    public Remove() {
      super(REGISTRY_NAME + "_remove_data", 0, 1);
    }

    @Override
    public boolean call(IContext context, Strategy[] ss, IStrategoTerm[] ts) throws InterpreterException {
      IStrategoTerm term = context.current();
      IStrategoTerm key = ts[0];

      if (key.getTermType() != IStrategoTerm.STRING)
        throw new InterpreterException(this.getName() + " requires a string as term argument");

      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return false;

      IStrategoTerm val = map.remove(Term.asJavaString(key));
      if (val == null)
        return false;
      
      context.setCurrent(val);
      return true;
    }
  }
  
  public static class GetAll extends AbstractPrimitive {
    public GetAll() {
      super(REGISTRY_NAME + "_get_all_data", 0, 0);
    }

    @Override
    public boolean call(IContext context, Strategy[] ss, IStrategoTerm[] ts) throws InterpreterException {
      IStrategoTerm term = context.current();
      
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return false;

      StrategoHashMap smap = new StrategoHashMap(map.size(), map.size());
      for (Entry<String, IStrategoTerm> e : map.entrySet()) {
        smap.put(new StrategoString(e.getKey(), TermFactory.EMPTY_LIST, IStrategoTerm.IMMUTABLE), e.getValue());
      }
      
      context.setCurrent(smap);
      return true;
    }
  }
  
  public static class PutAll extends AbstractPrimitive {
    public PutAll() {
      super(REGISTRY_NAME + "_put_all_data", 0, 1);
    }

    @Override
    public boolean call(IContext context, Strategy[] ss, IStrategoTerm[] ts) throws InterpreterException {
      IStrategoTerm term = context.current();
      IStrategoTerm arg = ts[0];
      
      if (arg.getTermType() != IStrategoTerm.BLOB || !(arg instanceof StrategoHashMap))
        throw new InterpreterException(this.getName() + " requires a hashmap as term argument");
      StrategoHashMap smap = (StrategoHashMap) arg;
      
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null) {
        map = new HashMap<String, IStrategoTerm>();
        analysisData.put(term, map);
      }

      for (Entry<IStrategoTerm, IStrategoTerm> e : smap.entrySet()) {
        if (e.getKey().getTermType() != IStrategoTerm.STRING)
          throw new InterpreterException(this.getName() + " requires a hashmap with strings as keys");
        
        map.put(Term.asJavaString(e.getKey()), e.getValue());
      }
      
      return true;
    }
  }
}
