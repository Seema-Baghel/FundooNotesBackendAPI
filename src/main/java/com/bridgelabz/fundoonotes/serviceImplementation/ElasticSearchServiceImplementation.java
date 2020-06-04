package com.bridgelabz.fundoonotes.serviceImplementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bridgelabz.fundoonotes.configuration.ElasticSearchConfig;
import com.bridgelabz.fundoonotes.exception.NoteException;
import com.bridgelabz.fundoonotes.model.NoteModel;
import com.bridgelabz.fundoonotes.model.UserModel;
import com.bridgelabz.fundoonotes.repository.NoteRepository;
import com.bridgelabz.fundoonotes.service.ElasticSearchService;

@Service
@SuppressWarnings("unchecked")
public class ElasticSearchServiceImplementation implements ElasticSearchService {

	@Autowired
	private NoteRepository noteRepository;
	

	@Autowired
	private ElasticSearchConfig config;
	
	@Autowired
	private ObjectMapper objectmapper;

	private static final  String INDEX = "springboot";

	private static final String TYPE = "notedetails";

	@Override
	public String createNote(NoteModel note) {
		Map<String, Object> notemapper = objectmapper.convertValue(note, Map.class);
		IndexRequest indexrequest = new IndexRequest(INDEX, TYPE, String.valueOf(note.getNoteId())).source(notemapper);
		IndexResponse indexResponse = null;
		try {
			indexResponse = config.client().index(indexrequest, RequestOptions.DEFAULT);
			System.out.printf(INDEX, indexResponse);
			return indexResponse.getResult().name();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		throw new NoteException("Cannot create note");
	
	}

	@Override
	public String updateNote(NoteModel note) {
		Map<String, Object> notemapper = objectmapper.convertValue(note, Map.class);
		System.out.println(note.getNoteId());
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, String.valueOf(note.getNoteId())).doc(notemapper);
		UpdateResponse updateResponse = null;
		try {
		updateResponse = config.client().update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return updateResponse.getResult().name();

		
	}

	@Override
	public String deleteNote(UserModel note) {
		Map<String, Object> notemapper = objectmapper.convertValue(note, Map.class);
		DeleteRequest deleterequest = new DeleteRequest(INDEX, TYPE, String.valueOf(note.getNotes()));
		DeleteResponse deleteResponse = null;
		try {
		deleteResponse = config.client().delete(deleterequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			System.out.printf(e.getMessage());
		}
		return deleteResponse.getResult().name();
		
	}

	@Override
	public List<NoteModel> searchbytitle(String title) throws IOException {
		SearchRequest searchrequest = new SearchRequest("springboot");
		SearchSourceBuilder searchsource = new SearchSourceBuilder();

		searchsource.query(QueryBuilders.matchQuery("title", title));
		searchrequest.source(searchsource);
		SearchResponse searchresponse = config.client().search(searchrequest, RequestOptions.DEFAULT);
		return getResult(searchresponse);
	}

		private List<NoteModel> getResult(SearchResponse searchresponse) {
		SearchHit[] searchhits = searchresponse.getHits().getHits();
		List<NoteModel> notes = new ArrayList<>();
		if (searchhits.length > 0) {
		Arrays.stream(searchhits)
		.forEach(hit -> notes.add(objectmapper.convertValue(hit.getSourceAsMap(), NoteModel.class)));
		}
		return notes;
		}

	
}
