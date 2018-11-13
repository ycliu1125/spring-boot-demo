package com.torr.service;

import com.torr.domain.KonoSuba;
import com.torr.repository.KonoSubaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class KonoSubaServiceTest {

    @Resource
    private KonoSubaService konoSubaService;
    @Resource
    private KonoSubaRepository konoSubaRepository;

    @Test
    public void testKonosuba() {
        //新增
        KonoSuba card1 = new KonoSuba();
        card1.setCardId("2020");
        card1.setName("Alice");
        card1.setHp(3865);
        card1.setAtk(2487);
        card1.setRec(655);
        card1.setSex(false);
        card1.setRace("Human");
        System.err.println(card1);
        card1 = konoSubaService.insert(card1);
        assertThat(card1).isNotNull();
        System.err.println(card1);

        //查詢By Id
        KonoSuba target = konoSubaService.findOne("2020");
        assertThat(target).isNotNull();
        System.err.println(target);

        //修改
        target.setName("Gill");
        target.setHp(6666);
        KonoSuba target2 = konoSubaService.update(target);
        assertThat(target2).isNotNull();
        System.err.println(target2);

        //查詢By Name
        KonoSuba target3 = konoSubaService.findOneByName("Gill");
        assertThat(target3).isNotNull();
        System.err.println(target3);

        //模糊查詢
        List<KonoSuba> cards = new ArrayList<>();
        cards = konoSubaService.findByNameContains("y");
        cards.forEach(System.err::println);

        //刪除
        konoSubaService.delete(target3);
        KonoSuba target4 = konoSubaService.findOne("2020");
        assertThat(target4).isNull();
        System.err.println(target4);
    }

    /**
     * 測試 Table 的各種關聯
     * 注意在獲取從屬關係(自我關聯)時 要設計取到一個終值
     * 否則JPA會將下一個關聯的屬性重複載入 導致記憶體爆炸(OverStackFlow)
     */
    @Test
    public void testKonosubaRelation() {
        KonoSuba konoSuba = konoSubaRepository.findByName("Aqua");
        System.err.println("Aqua(1882)第一個子項目的ID: " + konoSuba.getKonoSubas().get(0).getCardId());//OverStackFlow
        konoSuba.getKonoSubas().forEach(card -> System.err.println(" 子: " + card.getName() + " 父: " + card.getParent().getName()));
        konoSuba.getSkills().forEach(System.err::println);//關聯表中 Aqua 擁有的技能都列出來
    }
}
