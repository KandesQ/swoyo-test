package client;

import dto.TopicDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import util.CommandResolver;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends SimpleChannelInboundHandler<TopicDto> {
    private CountDownLatch latch = new CountDownLatch(1);

    private TopicDto responseTopicDto;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TopicDto topicDto) throws Exception {
        this.responseTopicDto = topicDto;
        latch.countDown();

    }

    /**
     * Ожидает ответа от сервера с заданным таймаутом
     */
    public Optional<TopicDto> waitResponse(long timeout, TimeUnit unit) throws InterruptedException {
        if (latch.await(timeout, unit)) {
            return Optional.of(responseTopicDto);
        }

//        reset(); ??

        return Optional.empty();
    }

    private void reset() {
        latch = new CountDownLatch(1);
        responseTopicDto = null;
    }
}
