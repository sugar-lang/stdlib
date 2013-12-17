package org.sugarj.transformations.analysis;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.IStrategoTuple;
import org.spoofax.interpreter.terms.ITermPrinter;
import org.spoofax.terms.StrategoList;
import org.spoofax.terms.StrategoString;
import org.spoofax.terms.StrategoTerm;
import org.spoofax.terms.StrategoTuple;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.JavaInteropRegisterer;
import org.strategoxt.lang.Strategy;

public class AnalysisDataInterop {

  public static AnalysisDataInterop instance = new AnalysisDataInterop();
  
  private AnalysisDataInterop() { }
  
  public Strategy[] getStrategies() {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData = new WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>>();
    return new Strategy[] {
        new get_analysis_data_0_1(analysisData),
        new put_analysis_data_0_2(analysisData), 
        new remove_analysis_data_0_1(analysisData),
        new get_all_analysis_data_0_0(analysisData),
        new put_all_analysis_data_0_1(analysisData),
        new get_all_analysis_data_as_list_0_0(analysisData)
      };
  }
  
  public JavaInteropRegisterer getInteropRegisterer() {
    class InteropRegisterer extends JavaInteropRegisterer{
      public InteropRegisterer() {
        super(getStrategies());
      }
    }
    return new InteropRegisterer();
  }

  

  public static class AnalysisDataTerm extends StrategoTerm {

    private static final long serialVersionUID = -4166241735936861836L;

    private Map<String, IStrategoTerm> analysisData;
    
    public AnalysisDataTerm(Map<String, IStrategoTerm> map) {
      super(IStrategoTerm.IMMUTABLE);
      this.analysisData = map;
    }
    
    @Override
    public IStrategoTerm[] getAllSubterms() {
      return new IStrategoTerm[] { };
    }

    @Override
    public IStrategoTerm getSubterm(int arg0) {
      return null;
    }

    @Override
    public int getSubtermCount() {
      return 0;
    }

    @Override
    public int getTermType() {
      return IStrategoTerm.BLOB;
    }

    @Override
    public void prettyPrint(ITermPrinter arg0) {
    }

    @Override
    public void writeAsString(Appendable arg0, int arg1) throws IOException {
      arg0.append("AnalysisData(" + analysisData.toString().replace('{', '[').replace('}', ']').replace('=', ',') + "),");
    }

    @Override
    public Iterator<IStrategoTerm> iterator() {
      return Collections.emptyIterator();
    }

    @Override
    protected boolean doSlowMatch(IStrategoTerm arg0, int arg1) {
      boolean res = arg0 instanceof AnalysisDataTerm 
                 && ((AnalysisDataTerm) arg0).analysisData.equals(analysisData);
      return res;
    }

    @Override
    protected int hashFunction() {
      return analysisData.hashCode();
    }
    
  }
  
  public class get_analysis_data_0_1 extends Strategy {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData;
    public get_analysis_data_0_1(WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }
    
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
  
  public class put_analysis_data_0_2 extends Strategy {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData;
    public put_analysis_data_0_2(WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

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
  
  public class remove_analysis_data_0_1 extends Strategy {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData;
    public remove_analysis_data_0_1(WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

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
  
  public class get_all_analysis_data_0_0 extends Strategy {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData;
    public get_all_analysis_data_0_0(WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return null;
      
      return new AnalysisDataTerm(new HashMap<String, IStrategoTerm>(analysisData.get(term)));
    }
  }
  
  public class put_all_analysis_data_0_1 extends Strategy {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData;
    public put_all_analysis_data_0_1(WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm arg) {
      if (arg.getTermType() != IStrategoTerm.BLOB || !(arg instanceof AnalysisDataTerm)) {
        context.getIOAgent().printError(this.getName() + " requires an analysis data as term argument");
        return null;
      }
      Map<String, IStrategoTerm> newmap = ((AnalysisDataTerm) arg).analysisData;
      
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null) {
        map = new HashMap<String, IStrategoTerm>();
        analysisData.put(term, map);
      }

      for (Entry<String, IStrategoTerm> e : newmap.entrySet()) {
        map.put(e.getKey(), e.getValue());
      }
      
      return term;
    }
  }
  
  public class get_all_analysis_data_as_list_0_0 extends Strategy {
    WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData;
    public get_all_analysis_data_as_list_0_0(WeakHashMap<IStrategoTerm, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      Map<String, IStrategoTerm> map = analysisData.get(term);
      if (map == null)
        return null;
      
      IStrategoList list = TermFactory.EMPTY_LIST;
      
      for (Entry<String, IStrategoTerm> e : map.entrySet()) {
        IStrategoString key = new StrategoString(e.getKey(), TermFactory.EMPTY_LIST, IStrategoTerm.IMMUTABLE);
        IStrategoTuple pair = new StrategoTuple(new IStrategoTerm[]{key, e.getValue()}, TermFactory.EMPTY_LIST, e.getValue().getStorageType());
        list = new StrategoList(pair, list, TermFactory.EMPTY_LIST, pair.getStorageType());
      }
      
      return list;
    }
  }
}
