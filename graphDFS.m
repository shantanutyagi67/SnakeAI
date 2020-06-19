clear all;
close all;
fid = fopen('C:\Users\geekSA67\code\SnakeAI\ScoreData.txt','rt');
fx1000='%f';
C = cell2mat(textscan(fid, fx1000,'Delimiter',';'));
fclose(fid);

figure(1)
meanC = mean(C',2)
plot(ones(length(C),1).*meanC)
hold on;
plot(C(:,1),'r')
ylabel('Score');
xlabel('Tests');
legend('values');
savefig('C:\Users\geekSA67\code\SnakeAI\DFS.fig')