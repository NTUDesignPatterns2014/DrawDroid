/*  
Author: Ren Peng Yuan        Peng_Y_Ren@163.com  
*/   
#include "AdvMorph.h"   
#include "lut_shrink.h"   
#include "lut_thin.h"   
   
   
static const MyType   SKEL1[]= {4,89,91,217,219};   
static const MyType   SKEL2[] = {8,152,153,216,217,408,409,472,473};   
static const MyType   SKEL3[] = {4,23,31,55,63};   
static const MyType  SKEL4[] = {8,26,27,30,31,90,91,94,95};   
static const MyType  SKEL5[] = {4,464,472,496,504};   
static const MyType  SKEL6[] = {8,50,51,54,55,306,307,310,311};   
static const MyType   SKEL7[] = {4,308,310,436,438};   
static const MyType   SKEL8[] = {8,176,180,240,244,432,436,496,500};   
   
#define KERNEL_TABLE {1, 8,  64,\
                      2, 16,128,\
                      4, 32, 256} 
                                                
   
AdvMorph::AdvMorph()    
{   
    use_shrink = false;   
    use_skel = false;   
    use_thin = false;   
    MyType kernel[] = KERNEL_TABLE;   
    memcpy(Kernel,kernel,sizeof(kernel));   
    tmp0 = tmp1 = tmp2 = tmp3 = NULL;   
    Thresh = 128;   
       
}   
   
AdvMorph::~AdvMorph()    
{   
    if(use_shrink)   
            delete [] shrink;   
   
    if(use_skel){   
        for(int i=0; i<8; i++)   
            delete [] skel[i];   
    }   
   
    if(use_thin){   
        delete [] thin[0];   
        delete [] thin[1];   
    }   
       
    if(tmp0)   
        cvReleaseMat(&tmp1);   
    if(tmp1)   
        cvReleaseMat(&tmp1);   
    if(tmp2)   
        cvReleaseMat(&tmp2);   
    if(tmp3)   
        cvReleaseMat(&tmp3);   
}   
   
bool AdvMorph::Identical(CvMat* mat1, CvMat* mat2)   
{   
    int i,j;   
    int rows = mat1->rows;   
    int cols = mat1->cols;   
    for(i=0; i<rows; i++){   
        MyType* ptr1 = (MyType*) (mat1->data.ptr + mat1->step*i);   
        MyType* ptr2 = (MyType*) (mat2->data.ptr + mat2->step*i);   
        for(j=0; j<cols; j++){   
            if(*ptr1++ != *ptr2++)   
                return false;   
        }   
    }   
    return true;   
}   
   
bool AdvMorph::HaveZero(CvMat* src)   
{   
    int i,j;   
    int rows = src->rows;   
    int cols = src->cols;   
    for(i=0; i<rows; i++){   
        MyType* ptr1 = (MyType*) (src->data.ptr + src->step*i);   
        for(j=0; j< cols; j++){   
            if(*ptr1++ == 0)   
                return true;   
        }   
    }   
    return false;   
}   
   
void AdvMorph::CheckTmp(CvMat** tmp, CvSize size)   
{   
    CvMat* Tmp = *tmp;   
    if(Tmp == NULL){   
        *tmp = cvCreateMat(size.height, size.width, MatType);   
        return;   
    }   
    CvSize size_tmp = cvGetSize(Tmp);   
    if((size_tmp.height != size.height)||(size_tmp.width != size.width)){   
        cvReleaseMat(tmp);   
        *tmp = cvCreateMat(size.height, size.width, MatType);   
    }   
}   
   
void AdvMorph::CheckShrink(void)   
{   
    if(!use_shrink){   
        MyType table[] = SHRINK_TABLE;   
        use_shrink = true;   
        shrink = new MyType[512];   
        memcpy(shrink,&table,512*sizeof(MyType));   
        for(int i=0; i<512; i++)   
            shrink[i] = !shrink[i];   
   
    }   
}   
   
void AdvMorph::Convolution(CvMat* src, CvMat* dst, MyType* kernel)   
{   
    int i,j,m,n;   
    int rows = src->rows;   
    int cols = src->cols;   
    for(i=1; i< rows-1; i++){   
        MyType* ptr0 = (MyType*) src->data.ptr+ cols * i + 1;   
        MyType* ptr1 = (MyType*) dst->data.ptr + cols* i + 1;   
        for(j=1; j<cols-1; j++){   
            *ptr1 = 0;   
            for(m=-1; m<= 1; m++){   
                for(n=-1; n<=1; n++){   
                    MyType* ptr2 = ptr0 + cols*m + n;   
                    *ptr1 += (*ptr2) * kernel[3*(m+1) + (n+1)];   
                    if(*ptr1 < 0)   
                        int a=1;   
                }   
            }   
            ptr0++;   
            ptr1++;   
        }   
    }   
}   
   
   
void AdvMorph::LUT(CvMat* src, CvMat* dst, MyType* lut)   
{   
    int i,j;   
    int rows = src->height;   
    int cols = src->width;   
    for(i=0; i<rows;i++){   
        MyType* ptr0 = (MyType*) (src->data.ptr + src->step * i);   
        MyType* ptr1 = (MyType*) (dst->data.ptr + dst->step *i );   
        for(j=0; j< cols; j++){   
            *ptr1 = lut[*ptr0];   
            ptr1++;   
            ptr0++;   
        }   
    }   
}   
   
void AdvMorph::CombCopy(CvMat* src, CvMat* dst, CvPoint start)   
{   
    int i,j;   
    int rows = src->rows;   
    int cols = src->cols;   
    for( i= start.y; i< rows; i+=2){   
        MyType* ptr0 = (MyType*)(src->data.ptr + src->step*i) + start.x ;   
        MyType* ptr1 = (MyType*)(dst->data.ptr + dst->step*i) + start.x ;   
        for(j=start.x; j<cols; j+=2){   
            *ptr1 = *ptr0;   
            ptr0 += 2;   
            ptr1 += 2;   
        }   
    }       
}   
int AdvMorph::Shrink(CvArr* src,CvArr* dst, int iterate)   
{   
    CheckShrink();   
    CvSize size = cvGetSize(src);   
    CheckTmp(&tmp0,size);   
    cvConvert(src,tmp0);    
    size.height = size.height + 2;   
    size.width = size.width + 2;   
    CheckTmp(&tmp1,size);    
    CheckTmp(&tmp2,size);   
    cvRectangle(tmp2,cvPoint(0,0),cvPoint(size.width-1,size.height-1),cvScalar(0));   
    CheckTmp(&tmp3,size);   
    cvCopyMakeBorder(tmp0,tmp1,cvPoint(1,1),IPL_BORDER_REPLICATE);    
    Threshold(tmp1,tmp1,Thresh,1);   
    while( (iterate> 0) || (iterate < 0)){   
        if(iterate >0)   
            iterate--;   
        cvCopy(tmp1,tmp3);   
        // First subiteration   
        Convolution(tmp1,tmp2,Kernel);    
        LUT(tmp2,tmp2,shrink);   
        cvAnd(tmp1,tmp2,tmp2);    
        CombCopy(tmp2,tmp1,cvPoint(1,1));    
        // Second subiteration   
        Convolution(tmp1,tmp2,Kernel);    
        LUT(tmp2,tmp2,shrink);   
        cvAnd(tmp1,tmp2,tmp2);    
        CombCopy(tmp2,tmp1,cvPoint(2,2));    
        // Third subiteration   
        Convolution(tmp1,tmp2,Kernel);    
        LUT(tmp2,tmp2,shrink);   
        cvAnd(tmp1,tmp2,tmp2);    
        CombCopy(tmp2,tmp1,cvPoint(2,1));    
        // Fourth subiteration   
        Convolution(tmp1,tmp2,Kernel);    
        LUT(tmp2,tmp2,shrink);   
        cvAnd(tmp1,tmp2,tmp2);    
        CombCopy(tmp2,tmp1,cvPoint(1,2));    
        if( Identical(tmp3,tmp1))   
            break;   
    }   
   CvMat mask;   
   cvGetSubRect(tmp1,&mask,cvRect(1,1,size.width-2,size.height-2));   
   Threshold(tmp1,tmp1,0,255);   
   cvAnd(tmp0,&mask,tmp0);   
   cvConvert(tmp0,dst);   
   
    return 0;   
}   
   
IplImage* AdvMorph::Shrink(IplImage* src, int iterate)   
{   
    IplImage* dst= cvCloneImage(src);   
    Shrink(src,dst,iterate);   
    return dst;   
}   
   
void AdvMorph::CheckSkel(void)   
{   
    int i,j;   
    if(!use_skel){   
        use_skel = true;   
        for(i=0; i<8; i++){   
            skel[i] = new MyType[512];   
            for(j=0; j<512; j++)   
                skel[i][j] = 1;   
        }   
        for(i=1; i<=SKEL1[0]; i++)   
            skel[0][SKEL1[i]] = 0;   
        for(i=1; i<=SKEL2[0]; i++)   
            skel[1][SKEL2[i]] = 0;   
        for(i=1; i<=SKEL3[0]; i++)   
            skel[2][SKEL3[i]] = 0;   
        for(i=1; i<=SKEL4[0]; i++)   
            skel[3][SKEL4[i]] = 0;   
        for(i=1; i<=SKEL5[0]; i++)   
            skel[4][SKEL5[i]] = 0;   
        for(i=1; i<=SKEL6[0]; i++)   
            skel[5][SKEL6[i]] = 0;   
        for(i=1; i<=SKEL7[0]; i++)   
            skel[6][SKEL7[i]] = 0;   
        for(i=1; i<=SKEL8[0]; i++)   
            skel[7][SKEL8[i]] = 0;            
    }   
}   
void AdvMorph::Threshold(CvMat* src, CvMat* dst, int Thr, int max)   
{   
    int i,j;   
    int rows = src->rows;   
    int cols = src->cols;   
    for(i=0; i<rows; i++){   
        MyType * ptr0 = (MyType *) (src->data.ptr) + cols* i;   
        MyType* ptr1 = (MyType *) (dst->data.ptr) + cols*i;   
        for(j=0; j<cols; j++){   
            if(*ptr0 > Thr)   
                *ptr1 = max;   
            else   
                *ptr1 = 0;   
            ptr0++;   
            ptr1++;   
        }   
    }   
}   
   
int AdvMorph::Skel(CvArr* src,CvArr* dst, int iterate)   
{     
    CheckSkel();   
    CvSize size = cvGetSize(src);   
    CheckTmp(&tmp0,size);   
    cvConvert(src,tmp0);    
    size.height = size.height + 2;   
    size.width = size.width + 2;   
    CheckTmp(&tmp1,size);    
    CheckTmp(&tmp2,size);   
    cvRectangle(tmp2,cvPoint(0,0),cvPoint(size.width-1,size.height-1),cvScalar(0));   
    CheckTmp(&tmp3,size);   
    cvCopyMakeBorder(tmp0,tmp1,cvPoint(1,1),IPL_BORDER_REPLICATE); //»òIPL_BORDER_CONSTANT   
    Threshold(tmp1,tmp1,Thresh,1);   
   
    while( (iterate> 0) || (iterate < 0)){   
        if(iterate >0)   
            iterate--;   
          
        cvCopy(tmp1,tmp3);   
        for(int i=0; i<8;i++){      
            Convolution(tmp1,tmp2,Kernel);    
            LUT(tmp2,tmp2,skel[i]);   
            cvAnd(tmp1,tmp2,tmp1);    
        }          
          
        if(Identical(tmp3,tmp1))   
            break;   
    }   
   CvMat mask;   
   cvGetSubRect(tmp1,&mask,cvRect(1,1,size.width-2,size.height-2));   
   Threshold(tmp1,tmp1,0,255);   
   cvAnd(tmp0,&mask,tmp0);   
   cvConvert(tmp0,dst);   
   
    return 0;   
}   
   
IplImage* AdvMorph::Skel(IplImage* src, int iterate)   
{   
    IplImage* dst= cvCloneImage(src);   
    Skel(src,dst,iterate);   
    return dst;   
}   
   
void AdvMorph::CheckThin(void)   
{   
    if(!use_thin){   
        MyType table1[512] = THIN_TABLE1;   
        MyType table2[512] = THIN_TABLE2;   
        use_thin = true;   
        thin[0] = new MyType[512];   
        thin[1] = new MyType[512];   
        memcpy(thin[0],&table1,512*sizeof(MyType));   
        memcpy(thin[1],&table2,512*sizeof(MyType));   
    }   
}   
   
int AdvMorph::Thin(CvArr* src,CvArr* dst, int iterate)   
{     
    CheckThin();   
    CvSize size = cvGetSize(src);   
    CheckTmp(&tmp0,size);   
    cvConvert(src,tmp0);    
    size.height = size.height + 2;   
    size.width = size.width + 2;   
    CheckTmp(&tmp1,size);    
    CheckTmp(&tmp2,size);   
    cvRectangle(tmp2,cvPoint(0,0),cvPoint(size.width-1,size.height-1),cvScalar(0));   
    CheckTmp(&tmp3,size);   
    cvCopyMakeBorder(tmp0,tmp1,cvPoint(1,1),IPL_BORDER_REPLICATE);    
    Threshold(tmp1,tmp1,Thresh,1);   
    while( (iterate> 0) || (iterate < 0)){   
        if(iterate >0)   
            iterate--;   
   
        cvCopy(tmp1,tmp3);   
        Convolution(tmp1,tmp2,Kernel);    
        LUT(tmp2,tmp2,thin[0]);   
        cvAnd(tmp1,tmp2,tmp1);    
   
        Convolution(tmp1,tmp2,Kernel);    
        LUT(tmp2,tmp2,thin[1]);   
        cvAnd(tmp1,tmp2,tmp1);    
   
        if(Identical(tmp3,tmp1))   
            break;   
    }   
   CvMat mask;   
   cvGetSubRect(tmp1,&mask,cvRect(1,1,size.width-2,size.height-2));   
   Threshold(tmp1,tmp1,0,255);   
   cvAnd(tmp0,&mask,tmp0);   
   cvConvert(tmp0,dst);   
   
    return 0;   
}   
   
IplImage* AdvMorph::Thin(IplImage* src, int iterate)   
{   
    IplImage* dst= cvCloneImage(src);  
    Thin(src,dst,iterate);   
    return dst;   
}  