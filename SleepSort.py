import time
import threading

a_list = [5,9,1,14,2,3,7,12]

class MyThread(threading.Thread):
    def run(self):
        time.sleep(int(self.getName()))
        print(self.getName())     
if __name__ == '__main__':
    for x in a_list:
        mythread = MyThread(name = x)
        mythread.start()
