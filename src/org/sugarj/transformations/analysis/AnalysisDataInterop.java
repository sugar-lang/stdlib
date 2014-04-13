package org.sugarj.transformations.analysis;

import java.io.IOException;
import java.io.Serializable;
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
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.terms.StrategoList;
import org.spoofax.terms.StrategoString;
import org.spoofax.terms.StrategoTerm;
import org.spoofax.terms.StrategoTuple;
import org.spoofax.terms.Term;
import org.spoofax.terms.TermFactory;
import org.spoofax.terms.attachments.AbstractTermAttachment;
import org.spoofax.terms.attachments.TermAttachmentType;
import org.spoofax.terms.attachments.VolatileTermAttachmentType;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.JavaInteropRegisterer;
import org.strategoxt.lang.Strategy;

public class AnalysisDataInterop {
  
  public static class TermKey implements Serializable {
    private static final long serialVersionUID = -1055204598368915963L;
  
    private IStrategoTerm term;
    private int offset;
    
    public TermKey(IStrategoTerm term) {
      this.term = term;
      IToken token = ImploderAttachment.getLeftToken(term);
      if (token != null)
        offset = token.getStartOffset();
    }
    
    @Override
    public int hashCode() {
      return 63 * offset + term.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
      return o instanceof TermKey && 
          ((TermKey) o).offset == offset &&
          ((TermKey) o).term.equals(term);
    }
    
    @Override
    public String toString() {
      return offset + "@" + term.toString();
    }
  }

  private Map<TermKey, Map<String, IStrategoTerm>> analysisData =
      new HashMap<TermKey, Map<String, IStrategoTerm>>();
  
  private Strategy[] strategies = new Strategy[] {
      new get_analysis_data_0_1(analysisData),
      new put_analysis_data_0_2(analysisData), 
      new remove_analysis_data_0_1(analysisData),
      new get_analysis_data_term_0_0(analysisData),
      new analysis_data_changed_0_1(analysisData),
      new get_all_analysis_data_as_list_0_0(analysisData),
      new load_analysis_data_0_0(analysisData),
      new clear_analysis_data_0_0(analysisData)
    };
  
  public AnalysisDataInterop() {
  }
  
  public AnalysisDataInterop(Map<TermKey, Map<String, IStrategoTerm>> map) {
    this();
    if (map != null)
      analysisData.putAll(map);
  }
  
  public Strategy[] getStrategies() {
    return strategies;
  }
  
  public void storeAnalysisData(IStrategoTerm term) {
    term.putAttachment(new AnalysisDataAttachment(analysisData));
  }
  
  public JavaInteropRegisterer createInteropRegisterer() {
    class InteropRegisterer extends JavaInteropRegisterer{
      public InteropRegisterer() {
        super(getStrategies());
      }
    }
    return new InteropRegisterer();
  }

  

  public static class DataTerm<T> extends StrategoTerm {

    private static final long serialVersionUID = -4166241735936861836L;

    private T data;
    
    public DataTerm(T data) {
      super(IStrategoTerm.IMMUTABLE);
      this.data = data;
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
      arg0.append("Data(" + data.toString().replace('{', '[').replace('}', ']').replace('=', ',') + "),");
    }

    @Override
    public Iterator<IStrategoTerm> iterator() {
      return Collections.emptyIterator();
    }

    @Override
    protected boolean doSlowMatch(IStrategoTerm arg0, int arg1) {
      @SuppressWarnings("unchecked")
      boolean res = arg0 instanceof DataTerm 
                 && ((DataTerm<T>) arg0).data.equals(data);
      return res;
    }

    @Override
    protected int hashFunction() {
      return data.hashCode();
    }
    
  }
  
  public static class AnalysisDataAttachment extends AbstractTermAttachment {

    private static final long serialVersionUID = -6951936326210493125L;

    public static final TermAttachmentType<AnalysisDataAttachment> TYPE = 
        new VolatileTermAttachmentType<>(AnalysisDataAttachment.class);
    
    private Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    
    public AnalysisDataAttachment() {
      this.analysisData = Collections.<TermKey, Map<String, IStrategoTerm>>emptyMap();
    }
    
    public AnalysisDataAttachment(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = new HashMap<TermKey, Map<String, IStrategoTerm>>(analysisData);
    }
    
    public Map<TermKey, Map<String, IStrategoTerm>> getAnalysisData() {
      return this.analysisData;
    }

    public static Map<TermKey, Map<String, IStrategoTerm>> getAnalysisData(IStrategoTerm term) {
      AnalysisDataAttachment at = term.getAttachment(TYPE);
      if (at == null)
        return null;
      return at.getAnalysisData();
    }
        
    @Override
    public TermAttachmentType<?> getAttachmentType() { return TYPE; }
  }
  
  public class get_analysis_data_0_1 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public get_analysis_data_0_1(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }
    
    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm key) {
      if (key.getTermType() != IStrategoTerm.STRING) {
        context.getIOAgent().printError(this.getName() + " requires a string as term argument");
        return null;
      }
      
      TermKey tkey = new TermKey(term);
      Map<String, IStrategoTerm> map = analysisData.get(tkey);
      if (map == null)
        return null;

      IStrategoTerm val = map.get(Term.asJavaString(key));
      if (val == null)
        return null;
    
      return val;
    }
  }
  
  public class put_analysis_data_0_2 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public put_analysis_data_0_2(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm key, IStrategoTerm val) {
      if (key.getTermType() != IStrategoTerm.STRING) {
        context.getIOAgent().printError(this.getName() + " requires a string as first term argument");
        return null;
      }

      TermKey tkey = new TermKey(term);
      Map<String, IStrategoTerm> map = analysisData.get(tkey);
      if (map == null) {
        map = new HashMap<String, IStrategoTerm>();
        analysisData.put(tkey, map);
      }
      
      map.put(Term.asJavaString(key), val);

      return term;
    }
  }
  
  public class remove_analysis_data_0_1 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public remove_analysis_data_0_1(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm key) {
      if (key.getTermType() != IStrategoTerm.STRING) {
        context.getIOAgent().printError(this.getName() + " requires a string as term argument");
        return null;
      }

      Map<String, IStrategoTerm> map = analysisData.get(new TermKey(term));
      if (map == null)
        return null;

      IStrategoTerm val = map.remove(Term.asJavaString(key));
      if (val == null)
        return null;
      
      return val;
    }
  }
  
  public class get_analysis_data_term_0_0 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public get_analysis_data_term_0_0(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      Map<TermKey, Map<String, IStrategoTerm>> data = new HashMap<>();
      
      for (Entry<TermKey, Map<String, IStrategoTerm>> e : analysisData.entrySet())
        data.put(e.getKey(), new HashMap<>(e.getValue()));
      
      return new DataTerm<>(data);
    }
  }
  
  public class analysis_data_changed_0_1 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public analysis_data_changed_0_1(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term, IStrategoTerm arg) {
      if (arg.getTermType() != IStrategoTerm.BLOB || !(arg instanceof DataTerm<?>)) {
        context.getIOAgent().printError(this.getName() + " requires an analysis data as term argument");
        return null;
      }
      
      @SuppressWarnings("unchecked")
      Map<TermKey, Map<String, IStrategoTerm>> old = ((DataTerm<Map<TermKey, Map<String, IStrategoTerm>>>) arg).data;
      
      if (analysisData.equals(old))
        return null;
      else
        return term;
    }
  }
  
  public class get_all_analysis_data_as_list_0_0 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public get_all_analysis_data_as_list_0_0(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      Map<String, IStrategoTerm> map = analysisData.get(new TermKey(term));
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
  
  public class load_analysis_data_0_0 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public load_analysis_data_0_0(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      Map<TermKey, Map<String, IStrategoTerm>> termData = AnalysisDataAttachment.getAnalysisData(term);
      
      if (termData == null) {
        context.getIOAgent().printError(this.getName() + " could not load analysis-data attachment from term");
        return null;
      }
        
      for (Entry<TermKey, Map<String, IStrategoTerm>> e : termData.entrySet()) {
        Map<String, IStrategoTerm> omap = analysisData.get(e.getKey());
        if (omap == null)
          analysisData.put(e.getKey(), e.getValue());
        else
          omap.putAll(e.getValue());
      }
      
      return term;
    }
  }
  
  public class clear_analysis_data_0_0 extends Strategy {
    Map<TermKey, Map<String, IStrategoTerm>> analysisData;
    public clear_analysis_data_0_0(Map<TermKey, Map<String, IStrategoTerm>> analysisData) {
      this.analysisData = analysisData;
    }

    @Override
    public IStrategoTerm invoke(Context context, IStrategoTerm term) {
      analysisData.clear();
      return term;
    }
  }
}
