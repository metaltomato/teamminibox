package com.example.TeamPortfolio.repository;

import com.example.TeamPortfolio.domain.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    //@EntityGraph: 엔티티들은 서로 연관되어 있는 관계가 보통이며, 그 관계는 그래프로 표현이 가능하다.
    //EntityGraph는 JPA가 어떤 엔티티를 불러올 때 그 엔티티와 관계된 엔티티를 불러올 것인지에 대한 정보 제공
    @EntityGraph(attributePaths = "roleSet")

    //getWithRoles:Customer 클래스에서 권한을 가져오는 메서드(매개변수 멤버아이디(cid))
    //@Query(쿼리문): 데이터베이스의 Customer테이블에서 멤버아이디가 같고 소셜로 로그인하지 않은 데이터를 조회해달라는 쿼리문이다.
    @Query("select m from Customer m where m.cid = :cid and m.social = false")
    Optional<Customer> getWithRoles(@Param("cid") String cid);
    @EntityGraph(attributePaths = "roleSet")
    Optional<Customer> findByEmail(String email);

    //비밀번호 수정
    @Modifying
    @Transactional
    @Query("update Customer m set m.cpw = :cpw where m.cid = :cid")
    void updatePassword(@Param("cpw") String password, @Param("cid") String cid);
    // 등급설정
    Customer findByCid(String cid);
}
