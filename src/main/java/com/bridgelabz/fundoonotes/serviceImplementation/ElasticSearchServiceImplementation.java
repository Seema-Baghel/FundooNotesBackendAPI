package com.bridgelabz.fundoonotes.serviceImplementation;
//package com.fundoonotes.serviceImplementation;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fundoonotes.configuration.ElasticSearchConfig;
//import com.fundoonotes.exception.NoteException;
//import com.fundoonotes.model.NoteModel;
//import com.fundoonotes.repository.NoteRepository;
//import com.fundoonotes.service.ElasticSearchService;
//
//@Service
//@SuppressWarnings("unchecked")
//public class ElasticSearchServiceImplementation implements ElasticSearchService {
//
//	@Autowired
//	private NoteRepository noteRepository;
//	
//
//	@Autowired
//	private ElasticSearchConfig config;
//	
//	@Autowired
//	private ObjectMapper objectmapper;
//
//	private static final  String INDEX = "fundoo";
//
//	private static final String TYPE = "note";
//
//	@Override
//	public String createNote(NoteModel note) {
//		Map<String, Object> notemapper = objectmapper.convertValue(note, Map.class);
//		IndexRequest indexrequest = new IndexRequest(INDEX, TYPE, String.valueOf(note.getId())).source(notemapper);
//		IndexResponse indexResponse = null;
//		try {
//			indexResponse = config.client().index(indexrequest, RequestOptions.DEFAULT);
//			System.out.printf(INDEX, indexResponse);
//			return indexResponse.getResult().name();
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}
//		return null;
//	
//	}
//
//	@Override
//	public void updateNote(long noteId) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public String deleteNote(long noteId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<NoteModel> searchbytitle(String title) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	
//}
