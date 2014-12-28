/* 
Author: Ren Peng Yuan        Peng_Y_Ren@163.com 
*/ 
#include <cxcore.h> 
#include <cv.h> 
#pragma comment( lib, "cxcore.lib" )  
#pragma comment( lib, "cv.lib" )  
#pragma comment( lib, "highgui.lib" )  
typedef int MyType; 
#define MatType CV_32SC1 
 
class AdvMorph 
{ 
public: 
	AdvMorph(); 
	~AdvMorph(); 
	int Shrink(CvArr* src, CvArr* dst, int iterate = -1); 
	IplImage* Shrink(IplImage* src, int iterate = -1); 
	int Skel(CvArr* src, CvArr* dst, int iterate = -1); 
	IplImage* Skel(IplImage* src, int interate = -1); 
	int Thin(CvArr* src, CvArr* dst, int iterate = -1); 
	IplImage* Thin(IplImage* src, int interate = -1); 
	int Thresh; 
private: 
	void CheckSkel(void); 
	void CheckShrink(void); 
	void CheckThin(void); 
	void CheckTmp(CvMat** tmp, CvSize size); 
	void Convolution(CvMat* src, CvMat* dst, MyType* kernel); 
	void CombCopy(CvMat* src, CvMat* dst, CvPoint start); 
	bool Identical(CvMat* mat1, CvMat* mat2); 
	bool HaveZero(CvMat* src); 
	void LUT(CvMat* src, CvMat* dst, MyType* lut); 
	void Threshold(CvMat* src, CvMat* dst, int Thr, int max); 
	MyType Kernel[9]; 
	CvMat* tmp0;  
	CvMat* tmp1; 
	CvMat* tmp2; 
	CvMat* tmp3; 
	bool use_skel; 
	bool use_shrink; 
	bool use_thin; 
	MyType * skel[8]; 
    MyType * shrink; 
	MyType * thin[2]; 
};