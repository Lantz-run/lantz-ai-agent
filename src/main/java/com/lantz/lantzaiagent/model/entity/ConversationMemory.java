package com.lantz.lantzaiagent.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName conversation_memory
 */
@TableName(value ="conversation_memory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMemory implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = -7757577507770374460L;
    /**
     * 
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    @TableField(value = "conversationId")
    private String conversationId;

    /**
     * 
     */
    @TableField(value = "type")
    private String type;

    /**
     * 
     */
    @TableField(value = "memory")
    private String memory;

    /**
     * 
     */
    @TableField(value = "createdTime")
    private Date createdTime;

    /**
     * 
     */
    @TableField(value = "updatedTime")
    private Date updatedTime;

    /**
     * 
     */
    @TableField(value = "isDelete")
    private Integer isDelete;
}