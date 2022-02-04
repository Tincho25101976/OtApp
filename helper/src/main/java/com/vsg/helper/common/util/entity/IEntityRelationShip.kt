package com.vsg.helper.common.util.entity

import com.vsg.helper.common.model.IEntity

interface IEntityRelationShip<TParent, TChildren>
        where TParent : IEntity,
              TChildren : IEntity {

    val parent: TParent
    val children: List<TChildren>
}