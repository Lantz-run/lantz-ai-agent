package com.lantz.lantzaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.MessageAggregator;

/**
 * 自定义日志 Advisor
 * 打印 info 级别日志，只输出单次用户提示词和 AI 回复的文本
 */
@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

	@Override
	public String getName() {
		return "这是lantz自定义的拦截器";
	}

	@Override
	public int getOrder() {
		return 100;
	}

	private AdvisedRequest before(AdvisedRequest request) {
		log.info("AI request: {}", request.userText());
		return request;
	}

	private void observeAfter(AdvisedResponse advisedResponse) {
		log.info("AI response: {}", advisedResponse.response().getResult().getOutput().getText());
	}

	@Override
	public String toString() {
		return MyLoggerAdvisor.class.getSimpleName();
	}

	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

		// 1. 处理请求（前置处理）
		advisedRequest = this.before(advisedRequest);

		// 2. 调用链中下一个 Advisor
		AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

		// 3. 处理响应（后置处理）
		this.observeAfter(advisedResponse);

		return advisedResponse;
	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

		// 1. 处理前置请求
		advisedRequest = this.before(advisedRequest);

		// 2. 调用链中的下一个 Advisor 并处理流式响应
		Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

		return new MessageAggregator().aggregateAdvisedResponse(advisedResponses, this::observeAfter);
	}

}
