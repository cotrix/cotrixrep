package org.cotrix.io.sdmx;

import org.cotrix.domain.Codelist;
import org.cotrix.io.map.MapTask;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class SdmxMapTask implements MapTask<CodelistBean,SdmxMapDirectives> {
	
	@Override
	public Class<SdmxMapDirectives> directedBy() {
		return SdmxMapDirectives.class;
	}
	
	@Override
	public Codelist map(CodelistBean bean, SdmxMapDirectives directives) throws Exception {
		
		Sdmx2Codelist transform = new Sdmx2Codelist();
		
		return transform.apply(bean,directives);
	}
	
	@Override
	public String toString() {
		return "sdmx-mapper";
	}
}
