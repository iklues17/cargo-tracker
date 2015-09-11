package com.sds.fsf.cmm.web.rest.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for handling pagination.
 *
 * <p>
 * Pagination uses the same principles as the <a href="https://developer.github.com/v3/#pagination">Github API</api>,
 * and follow <a href="http://tools.ietf.org/html/rfc5988">RFC 5988 (Link header)</a>.
 * </p>
 */
public class PaginationUtil {

    public static final int DEFAULT_OFFSET = 1;

    public static final int MIN_OFFSET = 1;

    public static final int DEFAULT_LIMIT = 20;

    public static final int MAX_LIMIT = 100;

    public static Pageable generatePageRequest(Integer offset, Integer limit) {
        if (offset == null || offset < MIN_OFFSET) {
            offset = DEFAULT_OFFSET;
        }
        if (limit == null || limit > MAX_LIMIT) {
            limit = DEFAULT_LIMIT;
        }
        return new PageRequest(offset - 1, limit);
    }
    
    public static Pageable generatePageRequest(Integer offset, Integer limit, Sort sort) {
    	if (offset == null || offset < MIN_OFFSET) {
    		offset = DEFAULT_OFFSET;
    	}
    	if (limit == null || limit > MAX_LIMIT) {
    		limit = DEFAULT_LIMIT;
    	}
    	
    	return new PageRequest(offset - 1, limit, sort);
    }
    
    public static Pageable generatePageRequest(Integer offset, Integer limit, Direction direction, String... properties) {
    	if (offset == null || offset < MIN_OFFSET) {
    		offset = DEFAULT_OFFSET;
    	}
    	if (limit == null || limit > MAX_LIMIT) {
    		limit = DEFAULT_LIMIT;
    	}
    	
    	return new PageRequest(offset - 1, limit, direction, properties);
    }
    
    
    public static <T> MyBatisPage<T> generateMyBatisPageRequest(Integer offset, Integer limit) {
    	return new MyBatisPage<T>(generatePageRequest(offset, limit), null);
    }
    
    public static <T> MyBatisPage<T> generateMyBatisPageRequest(Integer offset, Integer limit, T prm) {
        return new MyBatisPage<T>(generatePageRequest(offset, limit), prm);
    }
    
    public static <T> MyBatisPage<T> generateMyBatisPageRequest(Integer offset, Integer limit, T prm, Sort sort) {
    	return new MyBatisPage<T>(generatePageRequest(offset - 1, limit, sort), prm);
    }
    
    public static <T> MyBatisPage<T> generateMyBatisPageRequest(Integer offset, Integer limit, T prm, Direction direction, String... properties) {
    	return new MyBatisPage<T>(generatePageRequest(offset - 1, limit, direction, properties), prm);
    }
    
//    public static Map<String, Integer> generatePageRequestMyBatis(Integer offset, Integer limit) {
//    	Map<String, Integer> map = new HashMap<String, Integer>();
//    	if (offset == null || offset < MIN_OFFSET) {
//    		offset = DEFAULT_OFFSET;
//    	}
//    	if (limit == null || limit > MAX_LIMIT) {
//    		limit = DEFAULT_LIMIT;
//    	}
//    	
//    	map.put("pageIndex", offset);
//    	map.put("pageSize", limit);
//    	
//    	return map;
//    }
    
    /*
     * Page 객체를 이용하여 X-Total-Count를 화면으로 전달.
     */
    public static HttpHeaders setTotalElements(Page page){
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("X-Total-Count", "" + page.getTotalElements());
    	return headers;
    }
    
    /*
     * Page 객체가 없을 때(Mybatis 사용 시 등) X-Total-Count를 전달받은 int 값으로 설정
     */
    public static HttpHeaders setTotalElements(int totalCount){
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("X-Total-Count", "" + totalCount);
    	return headers;
    }

    public static HttpHeaders generatePaginationHttpHeaders(Page page, String baseUrl, Integer offset, Integer limit)
        throws URISyntaxException {

        if (offset == null || offset < MIN_OFFSET) {
            offset = DEFAULT_OFFSET;
        }
        if (limit == null || limit > MAX_LIMIT) {
            limit = DEFAULT_LIMIT;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotalElements());
        String link = "";
        if (offset < page.getTotalPages()) {
            link = "<" + (new URI(baseUrl +"?page=" + (offset + 1) + "&per_page=" + limit)).toString()
                + ">; rel=\"next\",";
        }
        if (offset > 1) {
            link += "<" + (new URI(baseUrl +"?page=" + (offset - 1) + "&per_page=" + limit)).toString()
                + ">; rel=\"prev\",";
        }
        link += "<" + (new URI(baseUrl +"?page=" + page.getTotalPages() + "&per_page=" + limit)).toString()
            + ">; rel=\"last\"," +
            "<" + (new URI(baseUrl +"?page=" + 1 + "&per_page=" + limit)).toString()
            + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }
    

}
