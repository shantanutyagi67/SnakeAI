clear all;
close all;
fid = fopen('C:\Users\geekSA67\code\SnakeAI\MATLAB\ScoreDataDFS.txt','rt');
fx1000='%f';
C = cell2mat(textscan(fid, fx1000,'Delimiter',';'));
fclose(fid);
figure(1)
meanC = mean(C',2)
plot(ones(length(C),1).*meanC,'b')
hold on;
plot(C(:,1),'r')
ylabel('Score');
xlabel('Tests');
legend('Average','Score');
savefig('C:\Users\geekSA67\code\SnakeAI\MATLAB\DFS.fig')
saveas(gcf,'C:\Users\geekSA67\code\SnakeAI\Results\DFS.png')