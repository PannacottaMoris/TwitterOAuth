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

	@Scheduled(fixedRate = 1000 * 3600 * 24)
	public void updateNumOfRecords() {
		List<GoodsEntity> presentGoods = repository.findAll();
		boolean hit = false;
		arrivals.clear();
		discontinued.clear();

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
		System.out.println("New Goods Arrived :" + arrivals.size());
		System.out.println("Some Goods is dincontinued :" + discontinued.size());
		
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
