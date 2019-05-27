/*
 * ApiController.java
 * RESTfullAPIをコントロールする。
 */

package com.example.demo.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.QuerySpeficiations;
import com.example.demo.entity.GoodsEntity;
import com.example.demo.entity.SearchQuery;
import com.example.demo.exception.ExceptionCommon;
import com.example.demo.repository.GoodsRepository;

@RestController
@RequestMapping("/")
@CrossOrigin
public class ApiController {
	@Autowired
	private GoodsRepository repository;

	//デバッグ用 全てのレコードを出力
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    @ResponseBody
    public List<GoodsEntity> show() {
        return repository.findAll();
    }

    //デバッグ用 クエリを出力
    @RequestMapping(value = "/query", method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SearchQuery sq(Model model, @RequestBody SearchQuery query) throws Exception {
    	return query;
    }

    //商品データ追加
    @RequestMapping(path = "/add", method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public List<GoodsEntity> add(Model model, @RequestBody GoodsEntity good) {
    	//追加する商品データに漏れがないか確認する
    	//名前、説明、価格の全てが入力されていない場合はエラーとなる。
    	if(good.getName() == null || good.getDescription() == null || good.getPrice() == 0){
    		throw new ExceptionCommon("Your request is not enouth.");
    	}

    	//同じ商品（同一名の物）がすでにDB内に存在しているかを確認する
    	List<GoodsEntity> record = repository.findByName(good.getName());
    	if(record.size() != 0) {
    		throw new ExceptionCommon(good.getName() + " is already added");
    	}

    	//商品データをDBに登録する
        repository.save(good);

        //追加後の全商品レコードを返す
        return repository.findAll();
    }

    //商品検索
    @RequestMapping(path="/search", method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<GoodsEntity> search(Model model, @RequestBody SearchQuery query) {
    	if(!query.isValidQuery()) {
    		throw new ExceptionCommon("Nothing in the store");
    	}
    	//動的クエリを作成する。
    	//名前、説明、最低価格、最高価格のうち入力されているものをAND検索する。
    	List<GoodsEntity> result = repository.findAll(Specifications
    			.where(QuerySpeficiations.nameContains(query.getName()))
    			.and(QuerySpeficiations.descriptionContains(query.getDescription()))
    			.and(QuerySpeficiations.priceGreaterThanEqual(query.getMinPrice()))
    			.and(QuerySpeficiations.priceLessThanEqual(query.getMaxPrice())));
    	if (result.size() == 0) {
    		throw new ExceptionCommon("Nothing in the store");
    	}
    	return result;
    }

    //商品データ更新
	@RequestMapping(value="/update", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public List<GoodsEntity> update(Model model, @RequestBody GoodsEntity good) {
		//入力された商品データを確認する
		//名前、説明、価格の全てが入力されていない場合はエラーとなる。
		if(good.getName() == null || good.getDescription() == null || good.getPrice() == 0){
    		throw new ExceptionCommon("Your request is not enouth.");
    	}
    	List<GoodsEntity> target = repository.findByName(good.getName());
    	//更新対象データがDBに存在するかを確認する
    	if (target.size() == 0) {
    		throw new ExceptionCommon(good.getName() + " is nothing in the store");
    	}
    	//DBを更新する
    	for(GoodsEntity tar : target) {
    		tar.setPrice(good.getPrice());
    		tar.setDescription(good.getDescription());
    		repository.save(tar);
    	}
    	//更新後の全レコードを表示する
    	return repository.findAll();
    }

	//商品データ削除
	//名前が一致するものを削除する
    @RequestMapping(value="/delete", method = RequestMethod.POST,
    		consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public List<GoodsEntity> delete(Model model, @RequestBody GoodsEntity good) {
    	List<GoodsEntity> target = repository.findByName(good.getName());
    	for(GoodsEntity tar : target) {
    		repository.deleteById(tar.getId());
    	}
    	return repository.findAll();
    }
}