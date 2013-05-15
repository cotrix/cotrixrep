package org.cotrix.io.tabular.csv;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ingest.ImportTask;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.csv.CsvAsset;
import org.virtualrepository.csv.CsvTable;
import org.virtualrepository.tabular.Table;

// since model it's streamed, combines parser and model
public class CsvImportTask implements ImportTask<CsvAsset, CsvImportDirectives> {

	private final VirtualRepository repository;

	@Inject
	public CsvImportTask(VirtualRepository repository) {
		this.repository = repository;
	}

	@Override
	public Class<CsvImportDirectives> directedBy() {
		return CsvImportDirectives.class;
	}

	@Override
	public Codelist retrieve(CsvAsset asset, CsvImportDirectives directives) throws Exception {

		Table table = repository.retrieve(asset, Table.class);

		return transform(table, directives);
	}

	@Override
	public Codelist parse(InputStream stream, CsvImportDirectives directives) throws Exception {

		Table table = new CsvTable(directives.format(), stream);

		return transform(table, directives);
	}

	// helpers

	public Codelist transform(Table table, CsvImportDirectives directives) throws Exception {

		Csv2Codelist transform = new Csv2Codelist(directives);

		return transform.apply(table);
	}
}
