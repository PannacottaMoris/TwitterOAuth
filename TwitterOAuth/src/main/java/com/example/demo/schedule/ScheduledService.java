/*
 * ScheduleService.java
 * 定期処理を管理する。
 */


package com.example.demo.schedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entity.GoodsEntity;
import com.example.demo.repository.GoodsRepository;

@Service
public class ScheduledService {
	@Autowired
	private GoodsRepository repository;
	private List<GoodsEntity> lastGoods = new ArrayList<GoodsEntity>();
	private List<GoodsEntity> arrivals = new ArrayList<GoodsEntity>();
	private List<GoodsEntity> discontinued = new ArrayList<GoodsEntity>();

	//24時間毎の定期処理。起点は起動時。
	@Scheduled(fixedRate = 1000 * 3600 * 24)
	public void updateNumOfRecords() {
		List<GoodsEntity> presentGoods = repository.findAll();
		boolean hit = false;
		arrivals.clear();
		discontinued.clear();

		//追加された商品を取得
		for(GoodsEntity presentGood : presentGoods) {
			hit = false;
			for(GoodsEntity lastGood : lastGoods) {
				if (presentGood.getId() == lastGood.getId()) {
					hit = true;
				}
			}
			if (hit == false) {
				arrivals.add(presentGood);
			}
		}
		//削除された商品を取得
		for(GoodsEntity lastGood : lastGoods) {
			hit = false;
			for(GoodsEntity presentGood : presentGoods) {
				if (presentGood.getId() == lastGood.getId()) {
					hit = true;
				}
			}
			if (hit = false) {
				discontinued.add(lastGood);
			}
		}
		
		//デバッグメッセージ。取得した個数をそれぞれ表示。
		System.out.println("New Goods Arrived :" + arrivals.size());
		System.out.println("Some Goods is dincontinued :" + discontinued.size());
		
		//現時点のリストを保持。次回定期処理時に使用する。
		lastGoods.clear();
		for(GoodsEntity good: presentGoods) {
			lastGoods.add(good);
		}
	}

	public List<GoodsEntity> getArrivals() {
		return this.arrivals;
	}

	public List<GoodsEntity> getDiscontinued() {
		return this.discontinued;
	}
}
