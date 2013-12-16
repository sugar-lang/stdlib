package org.sugarj.transformations.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.spoofax.interpreter.library.ssl.StrategoHashMap;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.StrategoString;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.JavaInteropRegisterer;
import org.strategoxt.lang.Strategy;

public class InteropRegisterer extends JavaInteropRegisterer {

  public static Strategy[] strategies = new Strategy[] {
    get_analysis_data_0_1.instance,
    put_analysis_data_0_2.instance, 
    remove_analysis_data_0_1.instance,
    get_all_analysis_data_0_0.instance,
    put_all_analysis_data_0_1.instance
  };
  
  public static InteropRegisterer instance = new InteropRegisterer();
  
  private InteropRegisterer() { 
    super(strategies);
  }

  private final static WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData = new WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>>();

  public static class get_analysis_data_0_1 extends Strategy {
    public static get_analysis_data_0_1 instance = new get_analysis_data_0_1();
    private get_analysis_data_0_1() { }
    
    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm key) {
      if (key.getTermType() != IStrategoTerm.STRING) {
        context.getIOAgent().printError(this.getName() + " requires a string as term argument");
        return null;
      }

      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return null;

      IStrategoTerm val = map.get(Term.asJavaString(key));
      if (val == null)
        return null;
    
      return val;
    }
  }
  
  public static class put_analysis_data_0_2 extends Strategy {
    public static put_analysis_data_0_2 instance = new put_analysis_data_0_2 ();
    private put_analysis_data_0_2() { }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm key, IStrategoTerm val) {
      if (key.getTermType() != IStrategoTerm.STRING) {
        context.getIOAgent().printError(this.getName() + " requires a string as first term argument");
        return null;
      }

      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null) {
        map = new HashMap<String, IStrategoTerm>();
        analysisData.put(term, map);
      }
      
      map.put(Term.asJavaString(key), val);

      return term;
    }
  }
  
  public static class remove_analysis_data_0_1 extends Strategy {
    public static remove_analysis_data_0_1 instance = new remove_analysis_data_0_1(); 
    private remove_analysis_data_0_1() { }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm key) {
      if (key.getTermType() != IStrategoTerm.STRING) {
        context.getIOAgent().printError(this.getName() + " requires a string as term argument");
        return null;
      }

      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return null;

      IStrategoTerm val = map.remove(Term.asJavaString(key));
      if (val == null)
        return null;
      
      return val;
    }
  }
  
  public static class get_all_analysis_data_0_0 extends Strategy {
    public static get_all_analysis_data_0_0 instance = new get_all_analysis_data_0_0();
    private get_all_analysis_data_0_0() { }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return null;

      StrategoHashMap smap = new StrategoHashMap(map.size(), map.size());
      for (Entry<String, IStrategoTerm> e : map.entrySet()) {
        smap.put(new StrategoString(e.getKey(), TermFactory.EMPTY_LIST, IStrategoTerm.IMMUTABLE), e.getValue());
      }
      
      return smap;
    }
  }
  
  public static class put_all_analysis_data_0_1 extends Strategy {
    public static put_all_analysis_data_0_1 instance = new put_all_analysis_data_0_1();
    private put_all_analysis_data_0_1() { }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm arg) {
      if (arg.getTermType() != IStrategoTerm.BLOB || !(arg instanceof StrategoHashMap)) {
        context.getIOAgent().printError(this.getName() + " requires a hashmap as term argument");
        return null;
      }
      StrategoHashMap smap = (StrategoHashMap) arg;
      
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null) {
        map = new HashMap<String, IStrategoTerm>();
        analysisData.put(term, map);
      }

      for (Entry<IStrategoTerm, IStrategoTerm> e : smap.entrySet()) {
        if (e.getKey().getTermType() != IStrategoTerm.STRING) {
          context.getIOAgent().printError(this.getName() + " requires a hashmap with strings as keys");
          return null;
        }
        
        map.put(Term.asJavaString(e.getKey()), e.getValue());
      }
      
      return term;
    }
  }
}
