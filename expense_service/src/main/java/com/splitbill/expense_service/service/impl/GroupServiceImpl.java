package com.splitbill.expense_service.service.impl;
import com.splitbill.expense_service.dto.request
        .CreateGroupRequest;
import com.splitbill.expense_service.dto.request.CreateParticipantRequest;
import com.splitbill.expense_service.dto.response
        .GroupResponse;

import com.splitbill.expense_service.dto.response
        .ParticipantResponse;

import com.splitbill.expense_service.entity.BillGroup;
import com.splitbill.expense_service.entity.Participant;

import com.splitbill.expense_service.repository
        .BillGroupRepository;
import com.splitbill.expense_service.service.GroupService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl
        implements GroupService {

    private final BillGroupRepository
            billGroupRepository;

    /**
     * Create new bill group with participants.
     */
    @Override
    @Transactional
    public GroupResponse createGroup(
            CreateGroupRequest request
    ) {

        log.info(
                "Creating group with name={}",
                request.getName()
        );

        BillGroup group =
                buildGroup(request);

        BillGroup savedGroup =
                billGroupRepository.save(group);

        log.info(
                "Group created successfully. groupId={}",
                savedGroup.getPublicId()
        );

        return toResponse(savedGroup);
    }

    private BillGroup buildGroup(
            CreateGroupRequest request
    ) {

        BillGroup group = new BillGroup();

        group.setName(
                request.getName()
        );

        addParticipants(
                group,
                request.getParticipants()
        );

        return group;
    }

    private void addParticipants(

            BillGroup group,

            List<CreateParticipantRequest>
                    participants
    ) {

        if (
                participants == null
                || participants.isEmpty()
        ) {

            return;
        }

        List<Participant> participantList =

                participants.stream()

                        .map(request -> {

                            Participant participant =
                                    new Participant();

                            participant.setName(
                                    request.getName()
                            );

                            participant.setGroup(group);

                            return participant;
                        })

                        .collect(Collectors.toList());

        group.getParticipants()
                .addAll(participantList);
    }

    private GroupResponse toResponse(
            BillGroup group
    ) {

        return GroupResponse.builder()

                .groupPublicId(
                        group.getPublicId()
                )

                .name(
                        group.getName()
                )

                .participants(
                        group.getParticipants()

                                .stream()

                                .map(
                                        this::toParticipantResponse
                                )

                                .collect(Collectors.toList())
                )

                .createdAt(
                        group.getCreatedAt()
                )

                .build();
    }

    private ParticipantResponse
    toParticipantResponse(
            Participant participant
    ) {

        return ParticipantResponse.builder()

                .participantPublicId(
                        participant.getPublicId()
                )

                .name(
                        participant.getName()
                )

                .build();
    }
}

