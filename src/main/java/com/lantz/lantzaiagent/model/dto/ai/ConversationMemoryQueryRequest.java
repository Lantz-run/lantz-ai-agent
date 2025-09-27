package com.lantz.lantzaiagent.model.dto.ai;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lantz.lantzaiagent.common.PageRequest;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName conversation_memory
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="conversation_memory")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMemoryQueryRequest extends PageRequest implements Serializable {

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
     * 用户id
     */
    @TableField(value = "userId")
    private String userId;

    /**
     * 
     */
    @TableField(value = "memory")
    private String memory;

    /**
     * 
     */
    @TableField(value = "isDelete")
    private Integer isDelete;
}