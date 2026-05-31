package com.splitbill.expense_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.splitbill.expense_service.dto.projection.ParticipantBalanceSummary;
import com.splitbill.expense_service.entity.BillGroup;
import com.splitbill.expense_service.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    boolean existsByPublicId(UUID publicId);

    List<Expense> findAllByGroup(BillGroup group);
    
    @Query(value = """
        SELECT

            p.public_id AS participantId,

            p.name AS participantName,

            COALESCE(
                SUM(
                    CASE
                        WHEN e.paid_by = p.id
                        THEN e.total_amount
                        ELSE 0
                    END
                ),
                0
            ) AS totalPaid,

            COALESCE(
                SUM(es.share_amount),
                0
            ) AS totalShare,

            COALESCE(
                SUM(
                    CASE
                        WHEN e.paid_by = p.id
                        THEN e.total_amount
                        ELSE 0
                    END
                ),
                0
            )
            -
            COALESCE(
                SUM(es.share_amount),
                0
            ) AS balance

        FROM participants p

        JOIN expense_splits es
            ON es.participant_id = p.id

        JOIN expenses e
            ON e.id = es.expense_id

        JOIN bill_groups bg
            ON bg.id = e.group_id

        WHERE bg.public_id = :groupPublicId

        GROUP BY
            p.public_id,
            p.name

        ORDER BY balance DESC
    """, nativeQuery = true)
    List<ParticipantBalanceSummary> getParticipantBalances(
            UUID groupPublicId
    );
}


